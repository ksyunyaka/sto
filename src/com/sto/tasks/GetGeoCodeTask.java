package com.sto.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.sto.utils.StoConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GetGeoCodeTask extends AsyncTask<String, Void, double[]> {

    @Override
    protected double[] doInBackground(String... params) {
        String url = params[0].replace("\n", " ").replace(" ", "%20");
        JSONObject locationAsJson = getLocationInfo(url);
        if( locationAsJson != null){
            return getGeoPoint(locationAsJson);
        } else{
            return StoConstants.DEFAULT_LOCATION;
        }
    }

    public JSONObject getLocationInfo(String address) {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + address + "&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (IOException e) {
            Log.e("ESTEO", "couldn't parse response for geo coding information", e);
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            Log.e("ESTEO", "couldn't get geo code information", e);
            return null;
        }

        return jsonObject;
    }


    public double[] getGeoPoint(JSONObject jsonObject) {
        double lon;
        double lat;
        try {

            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            Log.e("ESTEO", "couldn't parse JSON with geo code information", e);
            return StoConstants.DEFAULT_LOCATION;
        }
        return new double[]{lat, lon};
    }
}
