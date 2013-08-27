package com.sto.adapters;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sto.R;
import com.sto.entity.STO;
import com.sto.utils.StoCache;

/**
 * Created with IntelliJ IDEA.
 * User: Egor
 * Date: 10.08.13
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class STOInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater = null;

    public STOInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        STO stoByMarkerId = StoCache.INSTANCE.getStoByMarkerId(marker.getId());

        View infoWindow = inflater.inflate(R.layout.marker_info_window, null);


        TextView title = (TextView) infoWindow.findViewById(R.id.title);
        title.setText(stoByMarkerId.getTitle());

        if (!stoByMarkerId.getTelephone().equals("")) {
            TextView telephone = (TextView) infoWindow.findViewById(R.id.telephone);
            telephone.setText(Html.fromHtml(stoByMarkerId.getTelephone()));
        }

        if (!stoByMarkerId.getTime().equals("")) {
            TextView time = (TextView) infoWindow.findViewById(R.id.time);
            time.setText(Html.fromHtml(stoByMarkerId.getTime()));
        }

        TextView address = (TextView) infoWindow.findViewById(R.id.address);
        address.setText(Html.fromHtml(stoByMarkerId.getShortHistory()));

        if (!stoByMarkerId.getSite().equals("")) {
            TextView site = (TextView) infoWindow.findViewById(R.id.site);
            site.setClickable(true);
            site.setMovementMethod(LinkMovementMethod.getInstance());
            site.setText(Html.fromHtml("<a href='" + stoByMarkerId.getSite() + "'>" + stoByMarkerId.getSite() + "</a>"));
        }
        if (!stoByMarkerId.getWashType().equals("")) {
            TextView washType = (TextView) infoWindow.findViewById(R.id.wash);
            washType.setText(Html.fromHtml(stoByMarkerId.getWashType()));
        }
        return infoWindow;
    }
}
