package com.example.acerth.realrunner;

/**
 * Created by com on 21-Sep-15.
 */

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tab_Map extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Context mContext;
    GoogleMap mGoogleMap;
    MapView mMapView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    List<Location> mRoutePoints;
    boolean isStart = false;
    int startSec;
    int endSec;

    private TextView distance_text,time_text,timeWhileStart_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        mContext = getApplicationContext();
       mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(mContext.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGoogleMap = mMapView.getMap();
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mRoutePoints = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_distance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_start) {
            isStart = true;
            Calendar c = Calendar.getInstance();
            startSec = c.get(Calendar.SECOND);
            return true;
        } else if (id == R.id.action_stop) {
            isStart = false;
            Calendar c = Calendar.getInstance();
            endSec = c.get(Calendar.SECOND);
            getDistance();
            Log.d("dutation as second", Integer.toString(endSec - startSec));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Google Api Service Step
    // 1. Connect Google Service
    // 2. Wait for CallBack > mun ja pai call onConnected()
    // 3. Get Location for onLocationChanged

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (isStart) {
            Log.d("onLocationChanged", location.toString());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mRoutePoints.add(location);
            if (mRoutePoints.size() > 1) {
                getDistanceWhileStarting();
            }
        }
    }

    private double getDistance() {
        double distance = 0.0;
        int i = 0;
        float tempDistance = 0.0f;
        Location aLocation = null;
        Location bLocation = null;
        for (Location location : mRoutePoints) {
            if (i++ == 0) {
                aLocation = location;
            } else {
                bLocation = location;
                tempDistance = aLocation.distanceTo(bLocation);
                aLocation = bLocation;
            }
            distance += tempDistance;
        }
        Log.d("distance", Double.toString(distance));
        mRoutePoints.clear();
        return distance;
    }

    private double getDistanceWhileStarting() {
        double distance = 0.0;
        int i = 0;
        float tempDistance = 0.0f;
        Location aLocation = null;
        Location bLocation = null;
        for (Location location : mRoutePoints) {
            if (i++ == 0) {
                aLocation = location;
            } else {
                bLocation = location;
                tempDistance = aLocation.distanceTo(bLocation);
                aLocation = bLocation;
            }
            distance += tempDistance;
        }
        Log.d("You are now running: ", Double.toString(distance) + " meter.");
        return distance;
    }





}
