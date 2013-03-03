package com.sto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.sto.adapters.PlacesAutoCompleteAdapter;

public class STOActivity extends Activity implements OnSeekBarChangeListener, OnItemSelectedListener, AdapterView.OnItemClickListener {
    /**
     * Called when the activity is first created.
     */

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

        SeekBar sb = (SeekBar)findViewById(R.id.radiusSeekBar);
        sb.setMax(10);
        sb.setProgress(5);
        sb.setOnSeekBarChangeListener(this);

        final AutoCompleteTextView autoCompView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
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
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void start(){
        startActivity(new Intent(this, LocationFinderActivity.class));
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
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
