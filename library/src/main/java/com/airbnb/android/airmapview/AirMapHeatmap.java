package com.airbnb.android.airmapview;

import com.airbnb.android.airmapview.utils.heatmaps.HeatmapTileProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileProvider;

import java.util.Collection;

public class AirMapHeatmap {
  private final Collection<LatLng> data;
  private TileOverlay googleOverlay;
  private long id;

  public AirMapHeatmap(long id, Collection<LatLng> data) {
    this.id = id;
    this.data = data;
  }

  public TileProvider getTileProvider() {
    return new HeatmapTileProvider.Builder().data(data).build();
  }

  public void setGoogleOverlay(TileOverlay googleOverlay) {
    this.googleOverlay = googleOverlay;
  }

  public TileOverlay getGoogleOverlay() {
    return googleOverlay;
  }

  public long getId() {
    return id;
  }

  public Collection<LatLng> getData() {
    return data;
  }
}
