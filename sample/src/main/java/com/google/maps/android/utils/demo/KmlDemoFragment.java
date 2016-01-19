package com.google.maps.android.utils.demo;

import android.os.AsyncTask;
import android.util.Log;

import com.airbnb.airmapview.sample.R;
import com.airbnb.android.airmapview.utils.kml.KmlContainer;
import com.airbnb.android.airmapview.utils.kml.KmlLayer;
import com.airbnb.android.airmapview.utils.kml.KmlPlacemark;
import com.airbnb.android.airmapview.utils.kml.KmlPolygon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class KmlDemoFragment extends BaseDemoFragment {

    private KmlLayer kmlLayer;

    protected int getLayoutId() {
        return R.layout.kml_demo;
    }

    public void startDemo () {
        try {
            //retrieveFileFromResource();
            retrieveFileFromUrl();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void retrieveFileFromResource() {
//        try {
//            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.campus, getApplicationContext());
//            kmlLayer.addLayerToMap();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
    }

    private void retrieveFileFromUrl() {
        String url = "https://kml-samples.googlecode.com/svn/trunk/" +
                "morekml/Polygons/Polygons.Google_Campus.kml";
        new DownloadKmlFile(url).execute();
    }

    private void moveCameraToKml(KmlLayer kmlLayer) {
        //Retrieve the first container in the KML layer
        KmlContainer container = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        container = container.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        KmlPlacemark placemark = container.getPlacemarks().iterator().next();
        //Retrieve a polygon object in a placemark
        KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
        //Create LatLngBounds of the outer coordinates of the polygon
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
            builder.include(latLng);
        }
        getMap().animateCenterZoom(builder.build().getCenter(), 1);
    }

    private class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
        private final String mUrl;

        public DownloadKmlFile(String url) {
            mUrl = url;
        }

        protected byte[] doInBackground(String... params) {
            try {
                InputStream is =  new URL(mUrl).openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(byte[] byteArr) {
            try {
//                kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(byteArr),
//                        getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
