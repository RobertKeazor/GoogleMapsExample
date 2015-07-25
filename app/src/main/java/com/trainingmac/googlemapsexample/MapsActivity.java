package com.trainingmac.googlemapsexample;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trainingmac.googlemapsexample.asynktasks.GooglePlacesReadTask;
import com.trainingmac.googlemapsexample.asynktasks.ReadPointsTask;
import com.trainingmac.googlemapsexample.dto.Place;
import com.trainingmac.googlemapsexample.interfaces.AsyncSearchResponse;
import com.trainingmac.googlemapsexample.interfaces.AsyncTraceResponse;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener, AsyncSearchResponse, AsyncTraceResponse {

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);


    public final static String TAG = MapsActivity.class.getName();

    //Server Key
    private static final String GOOGLE_API_KEY = "AIzaSyCn--rUhEyIV8K0yLBvu8VWG47YlES-gHc";
    double currentLatitude = 0;
    double currentLongitude = 0;
    private int PROXIMITY_RADIUS = 50000; //Valor maximo permitido por el API

    private GoogleMap googleMap;
    private String[] mOptions;
    private List<String> mResults;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mDrawerResult;

    private ArrayAdapter resultAdapter;

    private List<Place> resultPlaces;

    private String destiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);

        mOptions = getResources().getStringArray(R.array.options);
        mResults = new ArrayList<>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_options);
        mDrawerResult = (ListView) findViewById(R.id.drawer_results);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptions));
        // Set the adapter for the list view
        resultAdapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mResults);
        mDrawerResult.setAdapter(resultAdapter);

        mDrawerResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "TRAZANDO RUTA...");

                TextView selectedOption = (TextView) view;
                destiny = selectedOption.getText().toString();

                LatLng destiny = new LatLng(resultPlaces.get(i).getLatitude(),resultPlaces.get(i).getLongitude());

                String url = getMapsApiDirectionsUrl(destiny);
                ReadPointsTask readPointsTask= new ReadPointsTask();
                readPointsTask.delegate = MapsActivity.this;
                readPointsTask.execute(url);

            }
        });


        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i(TAG, "BUSCANDO");

                TextView selectedOption = (TextView) view;
                String name = selectedOption.getText().toString();

                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + currentLatitude + "," + currentLongitude);
                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlacesUrl.append("&name=" + name);
                googlePlacesUrl.append("&sensor=true");
                googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

                Log.v(TAG, "URL:" + googlePlacesUrl.toString());

                /*GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                googlePlacesReadTask.delegate = MapsActivity.this;
                Object[] toPass = new Object[2];
                toPass[0] = googleMap;
                toPass[1] = googlePlacesUrl.toString();
                googlePlacesReadTask.execute(toPass);*/

            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }

        //Create market options and mark current location
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(currentLatitude, currentLongitude));
        options.title("Your position");
        googleMap.addMarker(options);

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLatitude, currentLongitude)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void searchFinish(List result){
        Log.i(TAG, "Actualiza lista de resultados");

        resultPlaces = (List<Place>) result;

        mResults.clear();
        for(Place place :  resultPlaces){
            mResults.add(place.getVicinity());
        }
        resultAdapter.notifyDataSetChanged();

        resultAdapter.notifyDataSetChanged();
    }

    @Override
    public void traceFinish(PolylineOptions route){
        Log.i(TAG, "Ruta trazada");

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private String getMapsApiDirectionsUrl(LatLng destiny) {
        String waypoints = "waypoints=optimize:true|"
                + currentLatitude + "," + currentLongitude
                + "|" + "|" + destiny.latitude + ","
                + destiny.longitude + "|";

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        Log.i(TAG, "Trace URL: " + url);
        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(BROOKLYN_BRIDGE)
                    .title("First Point"));
            googleMap.addMarker(new MarkerOptions().position(LOWER_MANHATTAN)
                    .title("Second Point"));
            googleMap.addMarker(new MarkerOptions().position(WALL_STREET)
                    .title("Third Point"));
        }
    }
}




