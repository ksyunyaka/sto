package com.sto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sto.entity.STO;
import com.sto.listeners.InfoClickListener;
import com.sto.utils.StoCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 3/2/13
 */
public class MapActivity extends FragmentActivity {

    private GoogleMap mMap;
    private LocationListener mlocListener = new MyLocationListener();
    private LocationManager mlocManager;
    private boolean isMyLocation;
    private double[] destCoordinates;
    private String category;
    private int radius;

    boolean gpsEnabled;
    boolean networkEnabled;

    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !networkEnabled) {
            buildAlertMessageNoGps();
        }

        Bundle extras = getIntent().getExtras();
        isMyLocation = extras.getBoolean(MainActivity.IS_MY_LOC);
        destCoordinates = extras.getDoubleArray(MainActivity.DEST_COORDINATES);
        category = extras.getString(MainActivity.CATEGORY);
        radius = extras.getInt(MainActivity.RADIUS)*1000;

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
            if (radius != -1) {
                float[] result = new float[3];
                Location.distanceBetween(startCoordinate.latitude, startCoordinate.longitude, entity.getLatitude(), entity.getLongitude(), result);
                if (radius >= result[0]) {
                    MarkerOptions mo = new MarkerOptions();
                    mo.position(new LatLng(entity.getLatitude(), entity.getLongitude()));
                    mo.title(entity.getTitle());
                    mo.icon(BitmapDescriptorFactory.fromResource(entity.getCategory().get(0).getResource()));
                    Marker marker = mMap.addMarker(mo);
                    StoCache.INSTANCE.addStoByMarker(marker.getId(), entity);
                }
            }
        }


        mMap.setOnInfoWindowClickListener(new InfoClickListener(this));

        CameraUpdate center = CameraUpdateFactory.newLatLng(startCoordinate);
        mMap.moveCamera(center);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
    }

    private LatLng getStartCoordinate() {
        LatLng coordinate;
        try {
            if (isMyLocation) {
                Location location = getCurrentGpsLocation();
                coordinate = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                coordinate = new LatLng(destCoordinates[0], destCoordinates[1]);
            }
        } catch (Exception e) {
            Log.e("ESTEO", "Couldn't get coordinates: " + e.getMessage());
            //in case of any troubles set current place to Kiev
            coordinate = new LatLng(50.450070, 30.523268);
        }
        return coordinate;
    }


    public Location getCurrentGpsLocation() {
        Location location = null;
        if (gpsEnabled) {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (networkEnabled && location == null) {
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
            location = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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


    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
//TODO delete old position of me or maybe add listener to the marker
//            LatLng coordinate = new LatLng(loc.getLatitude(), loc.getLongitude());
//            myMarker.remove();
//            new MarkerOptions().position(coordinate).title("Me");
//            myMarker = mMap.addMarker(new MarkerOptions().position(coordinate).title("Me"));
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}

