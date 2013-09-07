package com.sto.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.sto.route.DownloadTask;

import java.util.List;

public class ChangeLocationListener implements LocationListener {

    private Marker userMarker;
    private GoogleMap map;
    private Polyline route;

    public ChangeLocationListener(Marker userMarker, GoogleMap map) {
        this.userMarker = userMarker;
        this.map = map;
    }

    public void setRoute(Polyline route) {
        this.route = route;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        userMarker.remove();
        userMarker = map.addMarker(new MarkerOptions().position(coordinate).title("Я"));
        if (route != null) {
            List<LatLng> routePoints = route.getPoints();
            LatLng target = routePoints.get(routePoints.size() - 1);
            AsyncTask<LatLng, Void, PolylineOptions> routeTask = new DownloadTask().execute(new LatLng[]{coordinate, target});
            route.remove();
            try {
                PolylineOptions polylineOptions = routeTask.get();
                route = map.addPolyline(polylineOptions);
            } catch (Exception e) {
                Log.e("ESTEO", "can't build route after location changed", e);

            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
