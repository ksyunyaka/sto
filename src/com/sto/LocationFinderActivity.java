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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sto.entity.Category;
import com.sto.entity.STO;
import com.sto.utils.StoCache;

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
    private boolean isMyLocation;
    private double[] destCoordinates;
    private String [] categories;

    boolean gpsEnabled;
    boolean networkEnabled;

    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !networkEnabled) {
            buildAlertMessageNoGps();
        }

        Bundle extras = getIntent().getExtras();
        isMyLocation = extras.getBoolean("isMyLoc");
        destCoordinates = extras.getDoubleArray("destCoordinates");
        categories = extras.getStringArray("categories");


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
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        Location location = getLocation();

        List<STO> stoEntitiesToDisplay;
        if(categories.length == 0){
            stoEntitiesToDisplay = StoCache.INSTANCE.getAllSTOEntities();
        }else{
            stoEntitiesToDisplay = new ArrayList<>();
            for( String categoryName: categories){
                //todo if 1 item is in 2 categories which was selected - USE SET
                stoEntitiesToDisplay.addAll(StoCache.INSTANCE.getStoByCategory(categoryName));
            }
        }

        for (STO entity : stoEntitiesToDisplay) {
            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(entity.getCoordinateX(), entity.getCoordinateY()));
            mo.title(entity.getTitle());
            mo.icon(BitmapDescriptorFactory.fromResource(entity.getCategory().get(0).getResource()));
            Marker marker = mMap.addMarker(mo);
            StoCache.INSTANCE.addStoByMarker(marker.getId(), entity);
        }
         //todo default place
        LatLng coordinate;
        if (isMyLocation) {
            coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            coordinate = new LatLng(destCoordinates[0], destCoordinates[1]);
        }

        myMarker = mMap.addMarker(new MarkerOptions().position(coordinate).title("Me"));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                STO sto = StoCache.INSTANCE.getStoByMarkerId(marker.getId());

            }
        });

        CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
        mMap.moveCamera(center);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
    }


    public Location getLocation() {
        Location location = null;
        if (gpsEnabled) {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (networkEnabled && location == null) {
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
            location = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location == null) {
            //todo just for test: should build new alertMessage:couldn't find location
            buildAlertMessageNoGps();
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

