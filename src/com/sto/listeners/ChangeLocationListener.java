package com.sto.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.sto.utils.StoConstants;

public class ChangeLocationListener implements LocationListener {

    private Marker userMarker;
    private GoogleMap map;

    public ChangeLocationListener(Marker userMarker, GoogleMap map) {
        this.userMarker = userMarker;
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        userMarker.remove();
        userMarker = map.addMarker(new MarkerOptions().position(coordinate).title(StoConstants.USER_TITLE));

        float zoom = map.getCameraPosition().zoom;
        CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
        map.moveCamera(center);
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
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
