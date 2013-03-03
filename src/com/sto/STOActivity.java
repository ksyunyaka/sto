package com.sto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class STOActivity extends Activity implements OnSeekBarChangeListener,
		OnItemSelectedListener {
	/**
	 * Called when the activity is first created.
	 */

	public ArrayAdapter<String> adapter;
	public AutoCompleteTextView textview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
		ArrayAdapter<CharSequence> adapter_char = ArrayAdapter
				.createFromResource(this, R.array.layers_array,
						android.R.layout.simple_spinner_item);
		adapter_char
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter_char);

		spinner.setOnItemSelectedListener(this);

		SeekBar sb = (SeekBar) findViewById(R.id.radiusSeekBar);
		sb.setMax(10);
		sb.setProgress(5);
		sb.setOnSeekBarChangeListener(this);

		final EditText start_address = (EditText) findViewById(R.id.postal_address);
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.gps_rb:
					Toast.makeText(getApplicationContext(), "Button 1 pressed",
							Toast.LENGTH_SHORT).show();
					start_address.setEnabled(false);
					break;
				case R.id.address_rb:
					Toast.makeText(getApplicationContext(), "Button 2 pressed",
							Toast.LENGTH_SHORT).show();
					start_address.setEnabled(true);
					break;
				}

			}
		};

		((Button) findViewById(R.id.gps_rb)).setOnClickListener(listener);
		((Button) findViewById(R.id.address_rb)).setOnClickListener(listener);
        findViewById(R.id.main_screen_search_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

		// //test of db

		//
		// DBController dbController;
		// //is it ok to transfer inputStream
		// dbController = new DBController(this,
		// getResources().openRawResource(R.raw.insert_statements));
		// dbController.open();
		// try {
		// List<STO> values = dbController.getAllSTOEntities();
		//
		// // Spinner spinnerTest = (Spinner) findViewById(R.id.author_spinner);
		//
		// ArrayAdapter<STO> authorAdapter = new ArrayAdapter<STO>(this,
		// android.R.layout.simple_spinner_dropdown_item, values);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// // spinnerTest.setAdapter(authorAdapter);
		// // spinnerTest.setOnItemSelectedListener(this);
		// } finally {
		// dbController.close();
		//
		// }

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_list);
		final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
		
	
		GetPlaces task= new GetPlaces();
		adapter.setNotifyOnChange(true);
		textView.setAdapter(adapter);
		textView.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 1) {
					adapter.clear();
					GetPlaces task= new GetPlaces(); 
					// now pass the argument in the textview to the task
					task.execute(textView.getText().toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {

			}
		});
		
		

	}// onCreate
	
	class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {
	
		
		final AutoCompleteTextView textview = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
		
		
		@Override
		// three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args) {

			Log.d("gottaGo", "doInBackground");

			ArrayList<String> predictionsArr = new ArrayList<String>();
			
			try {
				URL googlePlaces = new URL(
				// URLEncoder.encode(url,"UTF-8");
						"https://maps.googleapis.com/maps/api/place/autocomplete/json?input=?sensor=false&key=AIzaSyBKCP3E40Ow86bjAYNByGiyy3bM9716PiY&components=country:ua&input="+URLEncoder.encode(args[0].toString(), "utf8"));
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(
						new InputStreamReader(tc.getInputStream()));

				String line;
				StringBuffer sb = new StringBuffer();
				// take Google's legible JSON and turn it into one big
				// string.
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				// turn that string into a JSON object
				JSONObject predictions = new JSONObject(sb.toString());
				// now get the JSON array that's inside that object
				JSONArray ja = new JSONArray(
						predictions.getString("predictions"));

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					// add each entry to our array
					predictionsArr.add(jo.getString("description"));
				}
			} catch (IOException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			} catch (JSONException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			}

			return predictionsArr;

		}

		// then our post

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			Log.d("YourApp", "onPostExecute : " + result.size());
			// update the adapter
			adapter = new ArrayAdapter<String>(getBaseContext(),
					R.layout.item_list);
			adapter.setNotifyOnChange(true);
			// attach the adapter to textview
			textview.setAdapter(adapter);

			for (String string : result) {

				Log.d("YourApp", "onPostExecute : result = " + string);
				adapter.add(string);
				adapter.notifyDataSetChanged();

			}

			Log.d("YourApp", "onPostExecute : autoCompleteAdapter"
					+ adapter.getCount());

		}// onPostExecute

	}// GetPlaces class
	
    private void start(){
        startActivity(new Intent(this, LocationFinderActivity.class));
    }
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// do nothing
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// do nothing

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser) {
        TextView tv = (TextView)findViewById(R.id.seekBarStatus);
        String value = null;
        if(progress == seekBar.getMax()){
            value ="max";
        }else {
            value = Integer.toString(progress+2)+" km";
        }
        tv.setText(value);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
