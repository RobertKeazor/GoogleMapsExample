package com.trainingmac.googlemapsexample.asynktasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trainingmac.googlemapsexample.interfaces.AsyncTraceResponse;
import com.trainingmac.googlemapsexample.utils.HttpHelper;
import com.trainingmac.googlemapsexample.utils.PathJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pepe on 7/22/15.
 */
public class TraceRouteTask extends
        AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    public AsyncTraceResponse delegate = null;

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(
            String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            PathJSONParser parser = new PathJSONParser();
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
        ArrayList<LatLng> points = null;
        PolylineOptions polyLineOptions = null;

        // traversing through routes
        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList<LatLng>();
            polyLineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = routes.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            polyLineOptions.addAll(points);
            polyLineOptions.width(2);
            polyLineOptions.color(Color.BLUE);
        }

        delegate.traceFinish(polyLineOptions);
    }
}