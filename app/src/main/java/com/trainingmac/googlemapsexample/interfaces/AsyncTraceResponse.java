package com.trainingmac.googlemapsexample.interfaces;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by pepe on 7/22/15.
 */
public interface AsyncTraceResponse {
    void traceFinish(PolylineOptions route);
}
