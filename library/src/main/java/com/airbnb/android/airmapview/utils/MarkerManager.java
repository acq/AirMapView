/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.airbnb.android.airmapview.utils;

import android.view.View;

import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.listeners.InfoWindowCreator;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerDragListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Keeps track of collections of markers on the map. Delegates all Marker-related events to each
 * collection's individually managed listeners.
 * <p/>
 * All marker operations (adds and removes) should occur via its collection class. That is, don't
 * add a marker via a collection, then remove it via Marker.remove()
 */
public class MarkerManager implements OnInfoWindowClickListener, OnMapMarkerClickListener, OnMapMarkerDragListener, InfoWindowCreator {
    private final AirMapView mMap;

    private final Map<String, Collection> mNamedCollections = new HashMap<>();
    private final Map<AirMapMarker, Collection> mAllMarkers = new HashMap<>();
    private final Map<Long, AirMapMarker> mIdsToMarkers = new HashMap<>();
    private final Map<Marker, AirMapMarker> mNativeMarkersToMarkers = new HashMap<>();

    public MarkerManager(AirMapView map) {
        this.mMap = map;
    }

    public Collection newCollection() {
        return new Collection();
    }

    /**
     * Create a new named collection, which can later be looked up by {@link #getCollection(String)}
     * @param id a unique id for this collection.
     */
    public Collection newCollection(String id) {
        if (mNamedCollections.get(id) != null) {
            throw new IllegalArgumentException("collection id is not unique: " + id);
        }
        Collection collection = new Collection();
        mNamedCollections.put(id, collection);
        return collection;
    }

    /**
     * Gets a named collection that was created by {@link #newCollection(String)}
     * @param id the unique id for this collection.
     */
    public Collection getCollection(String id) {
        return mNamedCollections.get(id);
    }

    @Override
    public View createInfoWindow(AirMapMarker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mInfoWindowCreator != null) {
            return collection.mInfoWindowCreator.createInfoWindow(marker);
        }
        return null;
    }

    @Override
    public View createInfoContents(AirMapMarker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mInfoWindowCreator != null) {
            return collection.mInfoWindowCreator.createInfoContents(marker);
        }
        return null;
    }

    @Override
    public void onInfoWindowClick(AirMapMarker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mInfoWindowClickListener != null) {
            collection.mInfoWindowClickListener.onInfoWindowClick(marker);
        }
    }

    @Override
    public void onMapMarkerClick(AirMapMarker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mMarkerClickListener != null) {
            collection.mMarkerClickListener.onMapMarkerClick(marker);
        }
    }

    @Override
    public void onMapMarkerDragStart(Marker marker) {
        Collection collection = mAllMarkers.get(getMapMarker(marker));
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMapMarkerDragStart(marker);
        }
    }

    @Override
    public void onMapMarkerDrag(Marker marker) {
        Collection collection = mAllMarkers.get(getMapMarker(marker));
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMapMarkerDrag(marker);
        }
    }

    @Override
    public void onMapMarkerDragEnd(Marker marker) {
        Collection collection = mAllMarkers.get(getMapMarker(marker));
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMapMarkerDragEnd(marker);
        }
    }

    @Override
    public void onMapMarkerDragStart(long id, LatLng latLng) {
        Collection collection = mAllMarkers.get(getMapMarker(id));
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMapMarkerDragStart(id, latLng);
        }
    }

    @Override
    public void onMapMarkerDrag(long id, LatLng latLng) {
        Collection collection = mAllMarkers.get(getMapMarker(id));
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMapMarkerDrag(id, latLng);
        }
    }

    @Override
    public void onMapMarkerDragEnd(long id, LatLng latLng) {
        Collection collection = mAllMarkers.get(getMapMarker(id));
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMapMarkerDragEnd(id, latLng);
        }
    }

    /**
     * Removes a marker from its collection.
     *
     * @param marker the marker to remove.
     * @return true if the marker was removed.
     */
    public boolean remove(AirMapMarker marker) {
        Collection collection = mAllMarkers.get(marker);
        return collection != null && collection.remove(marker);
    }

    public AirMapMarker getMapMarker(Marker marker) {
        return mNativeMarkersToMarkers.get(marker);
    }

    public AirMapMarker getMapMarker(long id) {
        return mIdsToMarkers.get(id);
    }

    public class Collection {
        private final Set<AirMapMarker> mMarkers = new HashSet<>();
        private OnInfoWindowClickListener mInfoWindowClickListener;
        private OnMapMarkerClickListener mMarkerClickListener;
        private OnMapMarkerDragListener mMarkerDragListener;
        private InfoWindowCreator mInfoWindowCreator;

        public Collection() {
        }

        public AirMapMarker addMarker(MarkerOptions opts) {
            AirMapMarker marker = new AirMapMarker.Builder<>(opts).build();
            mMap.addMarker(marker);
            mMarkers.add(marker);
            mAllMarkers.put(marker, Collection.this);
            mNativeMarkersToMarkers.put(marker.getMarker(), marker);
            mIdsToMarkers.put(marker.getId(), marker);
            return marker;
        }

        public boolean remove(AirMapMarker marker) {
            if (mMarkers.remove(marker)) {
                mAllMarkers.remove(marker);
                mIdsToMarkers.remove(marker.getId());
                mNativeMarkersToMarkers.remove(marker.getMarker());
                mMap.removeMarker(marker);
                return true;
            }
            return false;
        }

        public void clear() {
            for (AirMapMarker marker : mMarkers) {
                mMap.removeMarker(marker);
                mAllMarkers.remove(marker);
                mNativeMarkersToMarkers.remove(marker.getMarker());
                mIdsToMarkers.remove(marker.getId());
            }
            mMarkers.clear();
        }

        public java.util.Collection<AirMapMarker> getMarkers() {
            return Collections.unmodifiableCollection(mMarkers);
        }

        public void setOnInfoWindowClickListener(OnInfoWindowClickListener infoWindowClickListener) {
            mInfoWindowClickListener = infoWindowClickListener;
        }

        public void setOnMapMarkerClickListener(OnMapMarkerClickListener markerClickListener) {
            mMarkerClickListener = markerClickListener;
        }

        public void setOnMapMarkerDragListener(OnMapMarkerDragListener markerDragListener) {
            mMarkerDragListener = markerDragListener;
        }

        public void setOnInfoWindowCreator(InfoWindowCreator infoWindowCreator) {
            mInfoWindowCreator = infoWindowCreator;
        }
    }
}
