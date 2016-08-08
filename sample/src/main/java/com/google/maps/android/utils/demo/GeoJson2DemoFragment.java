package com.google.maps.android.utils.demo;

import android.util.Log;

import com.airbnb.airmapview.sample.R;
import com.airbnb.airmapview.sample.Util;
import com.airbnb.android.airmapview.utils.geojson.GeoJsonLayer;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoJson2DemoFragment extends BaseDemoFragment {

  private final static String TAG = "GeoJsonDemo2";

  @Override
  protected void startDemo() {
    // Draws a layer on top of Australia
    String geoJsonString = Util.readFromRawResource(getActivity(), R.raw.google);
    try {
      GeoJsonLayer layer = new GeoJsonLayer(getMap(), new JSONObject(geoJsonString));
      layer.addLayerToMap();
      getMap().animateCenterZoom(new LatLng(-25.56, 135.57), 3);
    } catch (JSONException e) {
      Log.e(TAG, "Failed to add GeoJson layer", e);
    }
  }
}
