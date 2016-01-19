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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.airmapview.sample.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private DemoAdapter mDemoAdapter = new DemoAdapter();
    private List<Demo> mDemos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mDemoAdapter);
        listView.setOnItemClickListener(this);

        addDemo("Clustering", new ClusteringDemoFragment());
        addDemo("Clustering: Custom Look", new CustomMarkerClusteringDemoFragment());
        addDemo("Clustering: 2K markers", new BigClusteringDemoFragment());
        addDemo("PolyUtil.decode", new PolyDecodeDemoFragment());
        addDemo("PolyUtil.simplify", new PolySimplifyDemoFragment());
        addDemo("IconGenerator", new IconGeneratorDemoFragment());
        addDemo("SphericalUtil.computeDistanceBetween", new DistanceDemoFragment());
        addDemo("Generating tiles", new TileProviderAndProjectionDemo());
        addDemo("Heatmaps", new HeatmapsDemoFragment());
        addDemo("Heatmaps with Places API", new HeatmapsPlacesDemoFragment());
        addDemo("GeoJSON Layer", new GeoJsonDemoFragment());
        addDemo("KML Layer Overlay", new KmlDemoFragment());
    }

    private void addDemo(String demoName, Fragment fragment) {
        mDemos.add(new Demo(demoName, fragment));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Demo demo = mDemoAdapter.getItem(position);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, demo.fragment).commit();
    }

    private class DemoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDemos.size();
        }

        @Override
        public Demo getItem(int position) {
            return mDemos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            Demo demo = getItem(position);
            ((TextView)convertView.findViewById(android.R.id.text1)).setText(demo.demoName);
            return convertView;
        }
    }

    private static class Demo {
        private final String demoName;
        private final Fragment fragment;

        public Demo(String demoName, Fragment fragment) {
            this.demoName = demoName;
            this.fragment = fragment;
        }
    }
}
