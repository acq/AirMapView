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
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setAdapter(mDemoAdapter);
        mListView.setOnItemClickListener(this);

        addDemo("Default", DefaultDemoFragment.class);
        addDemo("Clustering", ClusteringDemoFragment.class);
        addDemo("Clustering: Custom Look", CustomMarkerClusteringDemoFragment.class);
        addDemo("Clustering: 2K markers", BigClusteringDemoFragment.class);
        addDemo("PolyUtil.decode", PolyDecodeDemoFragment.class);
        addDemo("PolyUtil.simplify", PolySimplifyDemoFragment.class);
        addDemo("IconGenerator", IconGeneratorDemoFragment.class);
        addDemo("SphericalUtil.computeDistanceBetween", DistanceDemoFragment.class);
//        addDemo("Generating tiles", TileProviderAndProjectionDemo.class);
        addDemo("Ground Overlay", GroundOverlayDemoFragment.class);
        addDemo("Heatmaps", HeatmapsDemoFragment.class);
        addDemo("Heatmaps with Places API", HeatmapsPlacesDemoFragment.class);
        addDemo("GeoJSON Layer", GeoJsonDemoFragment.class);
        addDemo("GeoJSON Layer 2", GeoJson2DemoFragment.class);
        addDemo("KML Layer Overlay", KmlDemoFragment.class);

        DefaultDemoFragment defaultDemoFragment = new DefaultDemoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, defaultDemoFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void addDemo(String demoName, Class<? extends BaseDemoFragment> fragment) {
        mDemos.add(new Demo(demoName, fragment));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Demo demo = mDemoAdapter.getItem(position);
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, demo.fragment.newInstance()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListView.setSelection(position);
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        setTitle(demo.demoName);
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
        private final Class<? extends BaseDemoFragment> fragment;

        public Demo(String demoName, Class<? extends BaseDemoFragment> fragment) {
            this.demoName = demoName;
            this.fragment = fragment;
        }
    }
}
