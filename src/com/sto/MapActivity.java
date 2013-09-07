package com.sto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.sto.entity.STO;
import com.sto.listeners.ChangeLocationListener;
import com.sto.listeners.InfoClickListener;
import com.sto.route.DownloadTask;
import com.sto.utils.StoCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 3/2/13
 */
public class MapActivity extends FragmentActivity {

    private static final long REFRESH_FREQUENCY = TimeUnit.MINUTES.toMillis(1);


    private GoogleMap mMap;
    ChangeLocationListener locationListener;
    private boolean isMyLocation;
    private double[] startAddress;
    private String category;
    private int radius;

    boolean gpsEnabled;
    boolean networkEnabled;

    private Marker myMarker;
    private Polyline route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        isMyLocation = extras.getBoolean(MainActivity.IS_MY_LOC);
        startAddress = extras.getDoubleArray(MainActivity.START_ADDRESS);
        category = extras.getString(MainActivity.CATEGORY);
        radius = extras.getInt(MainActivity.RADIUS) * 1000;

        checkLocationProviderSettings();
        setUpMapIfNeeded();
        addLocationListener();
        mMap.setOnInfoWindowClickListener(new InfoClickListener(this, isMyLocation));
    }

    private void addLocationListener() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationListener != null) {
                locationManager.removeUpdates(locationListener);
            }
            locationListener = new ChangeLocationListener(myMarker, mMap);
            if (gpsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_FREQUENCY, 0, locationListener);
            } else if (networkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, REFRESH_FREQUENCY, 0, locationListener);
            }
        } catch (Exception e) {
            Log.e("ESTEO", "Can't set location updates listener", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationProviderSettings();
        addLocationListener();
        if(isMyLocation){
            locationListener.setRoute(null);
        }
    }

    private void checkLocationProviderSettings() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            buildAlertMessageNoGps();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                mMap.clear();
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        List<STO> stoEntitiesToDisplay;
        if (category == null) {
            stoEntitiesToDisplay = StoCache.INSTANCE.getAllSTOEntities();
        } else {
            stoEntitiesToDisplay = new ArrayList<>();
            stoEntitiesToDisplay.addAll(StoCache.INSTANCE.getStoByCategory(category));
        }

        LatLng startCoordinate = getStartCoordinate();
        myMarker = mMap.addMarker(new MarkerOptions().position(startCoordinate).title("Я"));

        for (STO entity : stoEntitiesToDisplay) {
            if (isEntityInRadius(entity, startCoordinate)) {
                MarkerOptions mo = new MarkerOptions();
                mo.position(new LatLng(entity.getLatitude(), entity.getLongitude()));
                mo.title(entity.getTitle());
                mo.icon(BitmapDescriptorFactory.fromResource(entity.getCategory().get(0).getResource()));
                Marker marker = mMap.addMarker(mo);
                StoCache.INSTANCE.addStoByMarker(marker.getId(), entity);
            }
        }

        CameraUpdate center = CameraUpdateFactory.newLatLng(startCoordinate);
        mMap.moveCamera(center);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
    }

    private boolean isEntityInRadius(STO entity, LatLng startCoordinate) {
        if (radius > 0) {
            float[] result = new float[3];
            Location.distanceBetween(startCoordinate.latitude, startCoordinate.longitude, entity.getLatitude(), entity.getLongitude(), result);
            float distance = result[0];
            if (distance <= radius) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private LatLng getStartCoordinate() {
        LatLng coordinate;
        try {
            if (isMyLocation) {
                Location location = getCurrentLocation();
                coordinate = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                coordinate = new LatLng(startAddress[0], startAddress[1]);
            }
        } catch (Exception e) {
            Log.e("ESTEO", "Couldn't get coordinates: " + e.getMessage());
            //in case of any troubles set current place to Kiev
            coordinate = new LatLng(50.450070, 30.523268);
        }
        return coordinate;
    }


    public Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (gpsEnabled) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (networkEnabled && location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void buildRoute(LatLng target) {
        if (route != null) {
            route.remove();
        }
        LatLng start = myMarker.getPosition();
        AsyncTask<LatLng, Void, PolylineOptions> routeTask = new DownloadTask().execute(new LatLng[]{start, target});
        try {
            PolylineOptions polylineOptions = routeTask.get();
            route = mMap.addPolyline(polylineOptions);
            locationListener.setRoute(route);
        } catch (Exception e) {
            Log.e("ESTEO", "can't build route", e);
        }
    }

}

