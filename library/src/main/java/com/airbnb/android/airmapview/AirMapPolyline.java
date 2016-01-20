package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Helper class for keeping record of data needed to display a polyline, as well as an optional
 * object T associated with the polyline.
 */
public class AirMapPolyline<T> {

  private static final int STROKE_WIDTH = 5;
  private static final int STROKE_COLOR = Color.BLUE;

  private T object;
  private long id;
  private String title;
  private final PolylineOptions polylineOptions;
  private Polyline googlePolyline;

  public AirMapPolyline(List<LatLng> points, long id) {
    this(null, points, id);
  }

  public AirMapPolyline(T object, List<LatLng> points, long id) {
    this(object, points, id, STROKE_WIDTH, STROKE_COLOR);
  }

  public AirMapPolyline(T object, List<LatLng> points, long id, int strokeWidth, int strokeColor) {
    this.object = object;
    this.id = id;
    polylineOptions = new PolylineOptions().addAll(points).width(strokeWidth).color(strokeColor);
  }

  public AirMapPolyline(T object, long id, PolylineOptions polylineOptions) {
    this.object = object;
    this.id = id;
    this.polylineOptions = polylineOptions;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<LatLng> getPoints() {
    return polylineOptions.getPoints();
  }

  public String getTitle() {
    return title;
  }

  public T getObject() {
    return object;
  }

  public int getStrokeWidth() {
    return (int) polylineOptions.getWidth();
  }

  public int getStrokeColor() {
    return polylineOptions.getColor();
  }

  /**
   * Add this polyline to the given {@link GoogleMap} instance
   *
   * @param googleMap the {@link GoogleMap} instance to which the polyline will be added
   */
  public void addToGoogleMap(GoogleMap googleMap) {
    // add the polyline and keep a reference so it can be removed
    googlePolyline = googleMap.addPolyline(polylineOptions);
  }

  /**
   * Remove this polyline from a GoogleMap (if it was added).
   *
   * @return true if the {@link Polyline} was removed
   */
  public boolean removeFromGoogleMap() {
    if (googlePolyline != null) {
      googlePolyline.remove();
      googlePolyline = null;
      return true;
    }
    return false;
  }

  public static class Builder<T> {

    private final PolylineOptions polylineOptions;
    private T object;
    private long id;

    public Builder() {
      polylineOptions = new PolylineOptions().color(STROKE_COLOR).width(STROKE_WIDTH);
    }

    public Builder(PolylineOptions polylineOptions) {
      this.polylineOptions = polylineOptions;
    }

    public Builder<T> object(T object) {
      this.object = object;
      return this;
    }

    public Builder<T> id(long id) {
      this.id = id;
      return this;
    }

    public Builder<T> strokeColor(int color) {
      polylineOptions.color(color);
      return this;
    }

    public Builder<T> strokeWidth(float width) {
      polylineOptions.width(width);
      return this;
    }
    
    public Builder<T> add(LatLng point) {
      polylineOptions.add(point);
      return this;
    }

    public Builder<T> add(LatLng... points) {
      polylineOptions.add(points);
      return this;
    }

    public Builder<T> addAll(@NonNull Iterable<LatLng> points) {
      polylineOptions.addAll(points);
      return this;
    }

    public AirMapPolyline<T> build() {
      return new AirMapPolyline<>(object, id, polylineOptions);
    }
  }
}
