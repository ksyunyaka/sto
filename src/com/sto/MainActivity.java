package com.sto;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.sto.adapters.PlacesAutoCompleteAdapter;
import com.sto.entity.Category;
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

//import com.google.android.maps.GeoPoint;


public class MainActivity extends Activity implements OnSeekBarChangeListener, OnItemSelectedListener, AdapterView.OnItemClickListener {

    public static final String IS_MY_LOC = "isMyLoc";
    public static final String START_ADDRESS = "startAddress";
    public static final String CATEGORY = "category";
    public static final String RADIUS = "radius";
    boolean isMyLocation = true;
    double[] startAddress;
    String categoryToDisplay;
    int radius;

    View button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Category.getCategoriesName());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(this);

        SeekBar sb = (SeekBar) findViewById(R.id.radiusSeekBar);
        sb.setMax(10);
        sb.setProgress(10);
        sb.setOnSeekBarChangeListener(this);

        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        button = findViewById(R.id.main_screen_search_Button);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.gps_rb:
                        autoCompView.setEnabled(false);
                        button.setEnabled(true);
                        break;
                    case R.id.address_rb:
                        autoCompView.setEnabled(true);
                        isMyLocation = false;
                        button.setEnabled(false);
                        break;
                }
            }
        };

        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_address));
        autoCompView.setOnItemClickListener(this);

        findViewById(R.id.gps_rb).setOnClickListener(listener);
        findViewById(R.id.address_rb).setOnClickListener(listener);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        try {
            startAddress = new GeoCode().execute(str).get();
        } catch (Exception e) {
            Log.e("ESTEO", "Can't retrive start address");
            startAddress = new double[]{50.450070, 30.523268};
        }  finally {
            button.setEnabled(true);
        }
    }

    private void start() {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra(IS_MY_LOC, isMyLocation);
        intent.putExtra(START_ADDRESS, startAddress);
        intent.putExtra(CATEGORY, categoryToDisplay);
        intent.putExtra(RADIUS, radius);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
        categoryToDisplay = label;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser) {
        TextView tv = (TextView) findViewById(R.id.seekBarStatus);
        String value = null;
        if (progress == seekBar.getMax()) {
            value = "max";
            radius = -1;
        } else {
            radius = progress + 2;
            value = radius + " km";
        }
        tv.setText(value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private class GeoCode extends AsyncTask<String, Void, double[]> {

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
                throw new RuntimeException();
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
                throw new RuntimeException();
            }
            return new double[]{lat, lon};
        }


        @Override
        protected double[] doInBackground(String... params) {
            return getGeoPoint(getLocationInfo(params[0].replace("\n", " ").replace(" ", "%20")));
        }

    }
}



