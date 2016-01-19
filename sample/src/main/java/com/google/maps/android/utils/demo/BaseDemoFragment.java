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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.airmapview.sample.R;
import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;

public abstract class BaseDemoFragment extends Fragment {
  private AirMapView mMap;

  protected int getLayoutId() {
    return R.layout.map;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(getLayoutId(), container, false);
  }

  @Override
  public void onResume() {
    super.onResume();
    setUpMapIfNeeded();
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
}
