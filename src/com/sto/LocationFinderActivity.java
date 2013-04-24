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
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sto.db.DBController;
import com.sto.entity.STO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 3/2/13
 */
public class LocationFinderActivity extends FragmentActivity {

    private GoogleMap mMap;
    private LocationListener mlocListener = new MyLocationListener();
    private LocationManager mlocManager;
    private boolean isMyLock;
    private double[] destCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            buildAlertMessageNoGps();
        }

        Bundle extras = getIntent().getExtras();
        isMyLock = extras.getBoolean("isMyLock");
        destCoordinates = extras.getDoubleArray("destCoordinates");

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
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
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        Location location = getLocation(LocationManager.GPS_PROVIDER);

        DBController dbController = new DBController(this, getResources().openRawResource(R.raw.insert_statements));
        dbController.open();


        List<STO> allSTOEntities = dbController.getAllSTOEntities();
        List<MarkerOptions> markerOptionsList = new ArrayList<MarkerOptions>();
        for (STO entity : allSTOEntities) {
            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(entity.getCoordinateX(), entity.getCoordinateY()));
            mo.title(entity.getTitle());
            mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.azs));
            mMap.addMarker(mo);
        }
        LatLng coordinate;
        if (isMyLock) {
            coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            coordinate = new LatLng(destCoordinates[0], destCoordinates[1]);
        }
        mMap.addMarker(new MarkerOptions().position(coordinate).title("Me"));

        CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
        mMap.moveCamera(center);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
    }


    public Location getLocation(String provider) {
        Location location = null;
        if (mlocManager.isProviderEnabled(provider)) {
            mlocManager.requestLocationUpdates(provider, 5000, 1, mlocListener);
            location = mlocManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, "Shit with gps", Toast.LENGTH_LONG).show();
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


    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {


        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}

