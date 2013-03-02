package com.sto;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.sto.db.DBController;
import com.sto.entity.STO;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class STOActivity extends Activity implements OnSeekBarChangeListener, OnItemSelectedListener {
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

        final EditText start_address = (EditText)findViewById(R.id.postal_address);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.gps_rb:
                        Toast.makeText(getApplicationContext(), "Button 1 pressed", Toast.LENGTH_SHORT).show();
                        start_address.setEnabled(false);
                        LocationFinder location_finder =  new LocationFinder();
                        location_finder.getLocation();
                        break;
                    case R.id.address_rb:
                        Toast.makeText(getApplicationContext(), "Button 2 pressed", Toast.LENGTH_SHORT).show();
                        start_address.setEnabled(true);
                        break;
                }

            }
        };

        ((Button)findViewById(R.id.gps_rb)).setOnClickListener(listener);
        ((Button)findViewById(R.id.address_rb)).setOnClickListener(listener);

//        //test of db

//
//        DBController dbController;
//        //is it ok to transfer inputStream
//        dbController = new DBController(this, getResources().openRawResource(R.raw.insert_statements));
//        dbController.open();
//        try {
//            List<STO> values = dbController.getAllSTOEntities();
//
////            Spinner spinnerTest = (Spinner) findViewById(R.id.author_spinner);
//
//            ArrayAdapter<STO> authorAdapter = new ArrayAdapter<STO>(this,
//                    android.R.layout.simple_spinner_dropdown_item, values);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////            spinnerTest.setAdapter(authorAdapter);
////            spinnerTest.setOnItemSelectedListener(this);
//        } finally {
//            dbController.close();
//
//        }

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
        if(progress == 10){
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
