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
import com.google.android.gms.maps.model.Marker;
import com.sto.R;
import com.sto.entity.STO;
import com.sto.utils.StoCache;

public class InfoClickListener implements GoogleMap.OnInfoWindowClickListener {

    private Context parent;

    public InfoClickListener(Context parentView) {
        this.parent = parentView;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        STO stoByMarkerId = StoCache.INSTANCE.getStoByMarkerId(marker.getId());

        AlertDialog.Builder dialogue = new AlertDialog.Builder(parent);
        dialogue.setTitle(marker.getTitle());
        dialogue.setIcon(R.drawable.app_icon);

        LinearLayout parentLayout = new LinearLayout(parent);
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        if (!stoByMarkerId.getTelephone().equals("")) {
            TextView telephone = new TextView(parent);
            telephone.setText(parent.getResources().getString(R.string.info_window_telephone) + Html.fromHtml(stoByMarkerId.getTelephone()).toString().replaceAll("\n", ""));
            parentLayout.addView(telephone);
        }

        if (!stoByMarkerId.getTime().equals("")) {
            TextView time = new TextView(parent);
            time.setText(parent.getResources().getString(R.string.info_window_time) + Html.fromHtml(stoByMarkerId.getTime()).toString().replaceAll("\n", ""));
            parentLayout.addView(time);
        }

        TextView address = new TextView(parent);
        address.setText(parent.getResources().getString(R.string.info_window_address) + Html.fromHtml(stoByMarkerId.getShortHistory()).toString().replaceAll("\n", ""));
        parentLayout.addView(address);

        if (!stoByMarkerId.getSite().equals("")) {
            TextView site = new TextView(parent);
            site.setClickable(true);
            SpannableString text = new SpannableString (parent.getResources().getString(R.string.info_window_site) + stoByMarkerId.getSite());
            Linkify.addLinks(text, Linkify.WEB_URLS);
            site.setText(text);
            site.setMovementMethod(LinkMovementMethod.getInstance());
            parentLayout.addView(site);
        }
        if (!stoByMarkerId.getWashType().equals("")) {
            TextView washType = new TextView(parent);
            washType.setText(parent.getResources().getString(R.string.info_window_wash_type) + Html.fromHtml(stoByMarkerId.getWashType()).toString().replaceAll("\n", ""));
            parentLayout.addView(washType);
        }

        dialogue.setView(parentLayout);
        dialogue.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        dialogue.show();

    }
}