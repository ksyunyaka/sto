package com.sto;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.google.android.maps.GeoPoint;
import com.sto.adapters.PlacesAutoCompleteAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class STOActivity extends Activity implements OnSeekBarChangeListener, OnItemSelectedListener, AdapterView.OnItemClickListener {
    /**
     * Called when the activity is first created.
     */
    boolean isMyLocation = true;
    double [] destinationAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        SeekBar sb = (SeekBar) findViewById(R.id.radiusSeekBar);
        sb.setMax(10);
        sb.setProgress(5);
        sb.setOnSeekBarChangeListener(this);

        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.gps_rb:
                        Toast.makeText(getApplicationContext(), "Button 1 pressed", Toast.LENGTH_SHORT).show();
                        autoCompView.setEnabled(false);
                        break;
                    case R.id.address_rb:
                        Toast.makeText(getApplicationContext(), "Button 2 pressed", Toast.LENGTH_SHORT).show();
                        autoCompView.setEnabled(true);
                        isMyLocation = false;
                        break;
                }

            }
        };

        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_list));
        autoCompView.setOnItemClickListener(this);

        findViewById(R.id.gps_rb).setOnClickListener(listener);
        findViewById(R.id.address_rb).setOnClickListener(listener);
        findViewById(R.id.main_screen_search_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

//        //test of db
//        DBController dbController;
//        //is it ok to transfer inputStream?
//        dbController = new DBController(this, getResources().openRawResource(R.raw.insert_statements));
//        dbController.open();
//

    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);

        new GeoCode().execute(str);

    }

    private void start() {
//        int[] addr = {destinationAddress.getLatitudeE6(), destinationAddress.getLongitudeE6()};
        Intent intent = new Intent(STOActivity.this, LocationFinderActivity.class);
        intent.putExtra("isMyLoc", isMyLocation);
        intent.putExtra("destCoordinates", destinationAddress);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        } else {
            value = Integer.toString(progress + 2) + " km";
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
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return jsonObject;
        }

        public double[] getGeoPoint(JSONObject jsonObject) {

            double lon = new Double(0);
            double lat = new Double(0);

            try {

                lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            destinationAddress = new double[]{lat,lon };
            return destinationAddress;

        }


        @Override
        protected double[] doInBackground(String... params) {
            return getGeoPoint(getLocationInfo(params[0].replace("\n", " ").replace(" ", "%20")));
        }

    }
}



