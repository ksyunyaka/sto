package com.sto.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sto.MapActivity;
import com.sto.R;
import com.sto.entity.STO;
import com.sto.utils.StoCache;
import com.sto.utils.StoConstants;

public class InfoClickListener implements GoogleMap.OnInfoWindowClickListener {

    private MapActivity parent;
    private boolean canBuildRoute;

    public InfoClickListener(MapActivity parentView, boolean canBuildRoute) {
        this.parent = parentView;
        this.canBuildRoute = canBuildRoute;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals(StoConstants.USER_TITLE)){
            return;
        }
        final STO entity = StoCache.INSTANCE.getStoByMarkerId(marker.getId());

        AlertDialog.Builder dialogue = new AlertDialog.Builder(parent);
        dialogue.setTitle(marker.getTitle());
        dialogue.setIcon(R.drawable.app_icon);

        LinearLayout parentLayout = new LinearLayout(parent);
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        if (!entity.getTelephone().equals("")) {
            TextView telephone = new TextView(parent);
            telephone.setText(parent.getResources().getString(R.string.info_window_telephone) + Html.fromHtml(entity.getTelephone()).toString().replaceAll("\n", ""));
            telephone.setTextSize(20);
            parentLayout.addView(telephone);
        }

        if (!entity.getTime().equals("")) {
            TextView time = new TextView(parent);
            time.setText(parent.getResources().getString(R.string.info_window_time) + Html.fromHtml(entity.getTime()).toString().replaceAll("\n", ""));
            time.setTextSize(20);
            parentLayout.addView(time);
        }

        TextView address = new TextView(parent);
        address.setText(parent.getResources().getString(R.string.info_window_address) + Html.fromHtml(entity.getShortHistory()).toString().replaceAll("\n", ""));
        address.setTextSize(20);
        parentLayout.addView(address);

        if (!entity.getSite().equals("")) {
            TextView site = new TextView(parent);
            site.setClickable(true);
            site.setTextSize(20);
            SpannableString text = new SpannableString (parent.getResources().getString(R.string.info_window_site) + entity.getSite());
            Linkify.addLinks(text, Linkify.WEB_URLS);
            site.setText(text);
            site.setMovementMethod(LinkMovementMethod.getInstance());
            parentLayout.addView(site);
        }
        if (!entity.getWashType().equals("")) {
            TextView washType = new TextView(parent);
            washType.setText(parent.getResources().getString(R.string.info_window_wash_type) + Html.fromHtml(entity.getWashType()).toString().replaceAll("\n", ""));
            washType.setTextSize(20);
            parentLayout.addView(washType);
        }

        dialogue.setView(parentLayout);
        dialogue.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        if(canBuildRoute){
            dialogue.setNeutralButton("Построить маршрут", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    parent.buildRoute(new LatLng(entity.getLatitude(), entity.getLongitude()));
                }
            });
        }

        dialogue.show();

    }
}