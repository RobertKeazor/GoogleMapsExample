package com.trainingmac.googlemapsexample.asynktasks;

import android.os.AsyncTask;
import android.util.Log;

import com.trainingmac.googlemapsexample.interfaces.AsyncSearchResponse;
import com.trainingmac.googlemapsexample.interfaces.AsyncTraceResponse;
import com.trainingmac.googlemapsexample.utils.HttpHelper;

/**
 * Created by pepe on 7/22/15.
 */
public class ReadPointsTask extends AsyncTask<String, Void, String> {

    public final static String TAG = ReadPointsTask.class.getName();

    public AsyncTraceResponse delegate = null;

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try {
            HttpHelper http = new HttpHelper();
            data = http.read(url[0]);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        TraceRouteTask traceRouteTask = new TraceRouteTask();
        traceRouteTask.delegate = delegate;
        traceRouteTask.execute(result);
    }
}
