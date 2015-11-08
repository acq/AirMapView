package com.airbnb.android.airmapview;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Helper class for keeping record of data needed to display a polyline, as well as an optional
 * object T associated with the polyline.
 */
public class AirMapPolyline<T> {

  private static final int STROKE_WIDTH = 1;
  private static final int STROKE_COLOR = Color.BLUE;

  private T object;
  private long id;
  private String title;
  private PolylineOptions polylineOptions;
  private Polyline        googlePolyline;

  public AirMapPolyline(List<LatLng> points, long id) {
    this(null, points, id);
  }

  public AirMapPolyline(T object, List<LatLng> points, long id) {
    this(object, points, id, STROKE_WIDTH, STROKE_COLOR);
  }

  public AirMapPolyline(T object, List<LatLng> points, long id, int strokeWidth, int strokeColor) {
    this.object = object;
    this.id = id;
    polylineOptions = new PolylineOptions();
    polylineOptions.addAll(points);
    polylineOptions.color(strokeColor);
    polylineOptions.width(strokeWidth);
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

  public void setPoints(List<LatLng> points) {
    polylineOptions.getPoints().clear();
    polylineOptions.getPoints().addAll(points);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
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
      return true;
    }
    return false;
  }

  public PolylineOptions getPolylineOptions() {
    return polylineOptions;
  }

  public Polyline getGooglePolyline() {
    return googlePolyline;
  }

  public void setGooglePolyline(Polyline googlePolyline) {
    this.googlePolyline = googlePolyline;
  }

  public static class Builder<T> {
    private final PolylineOptions polylineOptions = new PolylineOptions();
    private T    object;
    private long id;

    public Builder() {
      polylineOptions.width(STROKE_WIDTH);
      polylineOptions.color(STROKE_COLOR);
    }

    public Builder<T> object(T object) {
      this.object = object;
      return this;
    }

    public Builder<T> id(long id) {
      this.id = id;
      return this;
    }

    public Builder<T> color(int color) {
      polylineOptions.color(color);
      return this;
    }

    public Builder<T> width(float width) {
      this.polylineOptions.width(width);
      return this;
    }

    public Builder<T> geodesic(boolean geodesic) {
      this.polylineOptions.geodesic(geodesic);
      return this;
    }

    public Builder<T> zIndex(float zIndex) {
      this.polylineOptions.zIndex(zIndex);
      return this;
    }

    public Builder<T> visible(boolean visible) {
      this.polylineOptions.visible(visible);
      return this;
    }

    public Builder<T> add(LatLng point) {
      this.polylineOptions.add(point);
      return this;
    }

    public Builder<T> add(LatLng... points) {
      this.polylineOptions.add(points);
      return this;
    }

    public Builder<T> addAll(@NonNull Iterable<LatLng> points) {
      this.polylineOptions.addAll(points);
      return this;
    }

    public AirMapPolyline<T> build() {
      return new AirMapPolyline<>(object, id, polylineOptions);
    }
  }
}
