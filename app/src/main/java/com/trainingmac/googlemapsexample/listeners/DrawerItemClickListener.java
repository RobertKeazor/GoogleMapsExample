package com.trainingmac.googlemapsexample.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trainingmac.googlemapsexample.asynktasks.GooglePlacesReadTask;

/**
 * Created by pepe on 7/20/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private static final String GOOGLE_API_KEY = "AIzaSSDFSDF8Kv2eP0PM8adf5dSDFysdfas323SD3HA";
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }


    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {

    }


}