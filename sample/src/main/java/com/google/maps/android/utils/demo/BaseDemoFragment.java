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

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.airmapview.sample.R;
import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.GoogleChinaMapType;
import com.airbnb.android.airmapview.MapType;
import com.airbnb.android.airmapview.WebAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnCameraMoveListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnLatLngScreenLocationCallback;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;

public abstract class BaseDemoFragment extends Fragment
    implements OnCameraChangeListener,
    OnMapClickListener, OnCameraMoveListener, OnMapMarkerClickListener,
    OnInfoWindowClickListener, OnLatLngScreenLocationCallback {
  AirMapView mMap;
  DefaultAirMapViewBuilder mapViewBuilder;
  TextView textLogs;
  ScrollView logsScrollView;

  protected int getLayoutId() {
    return R.layout.default_demo;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mapViewBuilder = new DefaultAirMapViewBuilder(getContext());
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);

    textLogs = (TextView) view.findViewById(R.id.textLogs);
    logsScrollView = (ScrollView) view.findViewById(R.id.logsScrollView);

    associateButtonToMapType(view, R.id.btnMapTypeNormal, MapType.MAP_TYPE_NORMAL);
    associateButtonToMapType(view, R.id.btnMapTypeSattelite, MapType.MAP_TYPE_SATELLITE);
    associateButtonToMapType(view, R.id.btnMapTypeTerrain, MapType.MAP_TYPE_TERRAIN);

    return view;
  }

  private void associateButtonToMapType(View view, @IdRes int buttonId, final MapType mapType) {
    Button btnMapTypeNormal = (Button) view.findViewById(buttonId);
    if (btnMapTypeNormal != null) {
      btnMapTypeNormal.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View v) {
          getMap().setMapType(mapType);
        }
      });
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }


  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_main, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    AirMapInterface airMapInterface = null;

    switch (id) {
      case R.id.action_native_map:
        try {
          airMapInterface = mapViewBuilder.builder(AirMapViewTypes.NATIVE).build();
        } catch (UnsupportedOperationException e) {
          Toast.makeText(getContext(), "Sorry, native Google Maps are not supported by this device. " +
                  "Please make sure you have Google Play Services installed.",
              Toast.LENGTH_SHORT).show();
        }
        break;
      case R.id.action_mapbox_map:
        airMapInterface = mapViewBuilder.builder(AirMapViewTypes.WEB).build();
        break;
      case R.id.action_google_web_map:
        // force Google Web maps since otherwise AirMapViewTypes.WEB returns MapBox by default.
        airMapInterface = new WebAirMapViewBuilder().build();
        break;
      case R.id.action_google_china_web_map:
        airMapInterface = new WebAirMapViewBuilder().withOptions(new GoogleChinaMapType()).build();
        break;
      case R.id.action_clear_logs:
        if (textLogs != null) {
          textLogs.setText("");
        }
        break;
      default:
        break;
    }

    if (airMapInterface != null) {
      mMap.initialize(getChildFragmentManager(), airMapInterface);
    }

    return super.onOptionsItemSelected(item);
  }

  private void setUpMapIfNeeded() {
    if (mMap != null || getView() == null) {
      return;
    }
    mMap = (AirMapView) getView().findViewById(R.id.map);
    if (mMap != null) {
      mMap.setOnMapInitializedListener(new OnMapInitializedListener() {
        @Override
        public void onMapInitialized() {
          mMap.setOnMapClickListener(BaseDemoFragment.this);
          mMap.setOnCameraChangeListener(BaseDemoFragment.this);
          mMap.setOnCameraMoveListener(BaseDemoFragment.this);
          mMap.setOnMarkerClickListener(BaseDemoFragment.this);
          mMap.setOnInfoWindowClickListener(BaseDemoFragment.this);
          startDemo();
        }
      });
      DefaultAirMapViewBuilder mapViewBuilder = new DefaultAirMapViewBuilder(getActivity());
      AirMapInterface airMapInterface = mapViewBuilder.builder(AirMapViewTypes.NATIVE).build();
      if (airMapInterface != null) {
        mMap.initialize(getChildFragmentManager(), airMapInterface);
      }
    }
  }

  /**
   * Run the com.google.maps.android.utils.demo-specific code.
   */
  protected abstract void startDemo();

  protected AirMapView getMap() {
    setUpMapIfNeeded();
    return mMap;
  }

  void appendLog(String msg) {
    if (textLogs != null) {
      textLogs.setText(textLogs.getText() + "\n" + msg);
      logsScrollView.fullScroll(View.FOCUS_DOWN);
    }
  }

  @Override public void onCameraChanged(LatLng latLng, int zoom) {
    appendLog("Map onCameraChanged triggered with lat: " + latLng.latitude + ", lng: "
        + latLng.longitude);
  }

  @Override public void onMapClick(LatLng latLng) {
    if (latLng != null) {
      appendLog(
          "Map onMapClick triggered with lat: " + latLng.latitude + ", lng: "
              + latLng.longitude);

      getMap().getMapInterface().getScreenLocation(latLng, this);
    } else {
      appendLog("Map onMapClick triggered with null latLng");
    }
  }

  @Override public void onCameraMove() {
    appendLog("Map onCameraMove triggered");
  }

  @Override public void onMapMarkerClick(AirMapMarker marker) {
    appendLog("Map onMapMarkerClick triggered with marker " + marker.getId());
  }

  @Override public void onInfoWindowClick(AirMapMarker marker) {
    appendLog("Map onInfoWindowClick triggered with marker " + marker.getId());
  }

  @Override public void onLatLngScreenLocationReady(Point point) {
    appendLog("LatLng location on screen (x,y): (" + point.x + "," + point.y + ")");
  }
}
