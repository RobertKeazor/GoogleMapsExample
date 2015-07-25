package com.trainingmac.googlemapsexample.asynktasks;

/**
 * Created by pepe on 7/21/15.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trainingmac.googlemapsexample.dto.Place;
import com.trainingmac.googlemapsexample.interfaces.AsyncSearchResponse;
import com.trainingmac.googlemapsexample.utils.Places;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    public final static String TAG = PlacesDisplayTask.class.getName();

    public AsyncSearchResponse delegate = null;

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    List<Place> resultPlaces;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        Log.i(TAG, "Do In Background");

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            //mResults = (List<String>) inputObj[2];
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        Log.i(TAG, "On Post Executed");
        googleMap.clear();
        if (list != null) {
            Log.i(TAG, "Lista de " + list.size() + " elementos");
            //mResults.clear();
            resultPlaces.clear();
            for (int i = 0; i < list.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = list.get(i);
                //Create the DTO for place
                Place place = new Place();
                place.setPlaceName(googlePlace.get("place_name"));
                place.setVicinity(googlePlace.get("vicinity"));
                place.setLatitude(Double.parseDouble(googlePlace.get("lat")));
                place.setLongitude(Double.parseDouble(googlePlace.get("lng")));
                //Add Marker options to be put in the map
                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                markerOptions.position(latLng);
                markerOptions.title(place.getPlaceName() + " : " + place.getVicinity());
                googleMap.addMarker(markerOptions);
                //Add the place to the result list
                resultPlaces.add(place);
            }
        }
        delegate.searchFinish(resultPlaces);
    }
}
