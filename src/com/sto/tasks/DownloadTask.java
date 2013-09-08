package com.sto.tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sto.route.RouteJSONParser;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadTask extends AsyncTask<LatLng, Void, PolylineOptions> {

    @Override
    protected PolylineOptions doInBackground(LatLng... coordinates) {

        PolylineOptions polylineOptions = null;

        try {
             String url = getRouteUrl(coordinates);
            // Fetching the routeAsJson from web service
            JSONObject routeAsJson = downloadRouteAsJson(url);
            polylineOptions = parse(routeAsJson);
        } catch (Exception e) {
            Log.e("Background Task", e.toString());
        }
        return polylineOptions;
    }

    private String getRouteUrl(LatLng... coordinates){

        LatLng origin = coordinates[0];
        LatLng dest = coordinates[1];
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private JSONObject downloadRouteAsJson(String strUrl) throws IOException {
        JSONObject result = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            result = new JSONObject(sb.toString());

            br.close();

        } catch (Exception e) {
            Log.e("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return result;
    }

    private PolylineOptions parse(JSONObject jObject) {

        PolylineOptions polyLineOption = null;

        try {
            RouteJSONParser jsonParser = new RouteJSONParser();

            // Starts parsing data
            List<List<HashMap<String, String>>> routes = jsonParser.parse(jObject);
            polyLineOption = getPolyLineOption(routes);
            return polyLineOption;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return polyLineOption;
    }

    private PolylineOptions getPolyLineOption(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(3);
            lineOptions.color(Color.BLUE);
        }
        return  lineOptions;
    }

}

