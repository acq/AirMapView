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

package com.google.maps.android.utils.demo;

import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.airmapview.sample.R;
import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapPolyline;
import com.airbnb.android.airmapview.listeners.OnMapMarkerDragListener;
import com.airbnb.android.airmapview.utils.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;

public class DistanceDemoActivity extends BaseDemoActivity implements OnMapMarkerDragListener {
    private TextView mTextView;
    private AirMapMarker mMarkerA;
    private AirMapMarker mMarkerB;
    private AirMapPolyline mPolyline;

    @Override
    protected int getLayoutId() {
        return R.layout.distance_demo;
    }

    @Override
    protected void startDemo() {
        mTextView = (TextView) findViewById(R.id.textView);

        getMap().animateCenterZoom(new LatLng(-33.8256, 151.2395), 10);
        getMap().setOnMarkerDragListener(this);

        mMarkerA = new AirMapMarker.Builder<>(new MarkerOptions().position(new LatLng(-33.9046, 151.155)).draggable(true)).build();
        getMap().addMarker(mMarkerA);
        mMarkerB = new AirMapMarker.Builder<>(new MarkerOptions().position(new LatLng(-33.8291, 151.248)).draggable(true)).build();
        getMap().addMarker(mMarkerB);
        mPolyline = new AirMapPolyline.Builder<>(new PolylineOptions().geodesic(true)).build();
        getMap().addPolyline(mPolyline);

        Toast.makeText(this, "Drag the markers!", Toast.LENGTH_LONG).show();
        showDistance();
    }

    private void showDistance() {
        double distance = SphericalUtil.computeDistanceBetween(mMarkerA.getLatLng(), mMarkerB.getLatLng());
        mTextView.setText("The markers are " + formatNumber(distance) + " apart.");
    }

    private void updatePolyline() {
        getMap().removePolyline(mPolyline);
        mPolyline = new AirMapPolyline.Builder<>(new PolylineOptions().geodesic(true).add(mMarkerA.getLatLng()).add(mMarkerB.getLatLng())).build();
        getMap().addPolyline(mPolyline);
    }

    private String formatNumber(double distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }

    @Override
    public void onMapMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMapMarkerDrag(Marker marker) {
        showDistance();
        updatePolyline();
    }

    @Override
    public void onMapMarkerDragEnd(Marker marker) {
        showDistance();
        updatePolyline();
    }



    @Override
    public void onMapMarkerDragStart(long id, LatLng latLng) {

    }

    @Override
    public void onMapMarkerDrag(long id, LatLng latLng) {
        showDistance();
        updatePolyline();
    }

    @Override
    public void onMapMarkerDragEnd(long id, LatLng latLng) {
        showDistance();
        updatePolyline();
    }
}
