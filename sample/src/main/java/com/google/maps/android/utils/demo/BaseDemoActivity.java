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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.airbnb.airmapview.sample.R;
import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;

public abstract class BaseDemoActivity extends FragmentActivity {
  private AirMapView mMap;

  protected int getLayoutId() {
    return R.layout.map;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    setUpMapIfNeeded();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }

  private void setUpMapIfNeeded() {
    if (mMap != null) {
      return;
    }
    mMap = (AirMapView) findViewById(R.id.map);
    if (mMap != null) {
      mMap.setOnMapInitializedListener(new OnMapInitializedListener() {
        @Override
        public void onMapInitialized() {
          startDemo();
        }
      });
      DefaultAirMapViewBuilder mapViewBuilder = new DefaultAirMapViewBuilder(this);
      AirMapInterface airMapInterface = null;
      airMapInterface = mapViewBuilder.builder(AirMapViewTypes.NATIVE).build();
      if (airMapInterface != null) {
        mMap.initialize(getSupportFragmentManager(), airMapInterface);
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
}
