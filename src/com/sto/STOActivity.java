package com.sto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.sto.db.DBController;
import com.sto.entity.STO;
import org.w3c.dom.Comment;

import java.util.List;

public class STOActivity extends Activity implements OnItemSelectedListener {
	/** Called when the activity is first created. */
	
	private static class DemoDetails {
        /**
         * The resource id of the title of the demo.
         */
        private final int titleId;

        /**
         * The resources id of the description of the demo.
         */
        private final int descriptionId;

        /**
         * The demo activity's class.
         */
        private final Class<? extends android.support.v4.app.FragmentActivity> activityClass;

        public DemoDetails(int titleId, int descriptionId,
                Class<? extends android.support.v4.app.FragmentActivity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }
//	@Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        FeatureView featureView;
//        if (convertView instanceof FeatureView) {
//            featureView = (FeatureView) convertView;
//        } else {
//            featureView = new FeatureView(getContext());
//        }
//
//        DemoDetails demo = getItem(position);
//
//        featureView.setTitleId(demo.titleId);
//        featureView.setDescriptionId(demo.descriptionId);
//
//        return featureView;
//    }

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

        //test of db

        DBController dbController;
        dbController = new DBController(this);
        dbController.open();

        List<STO> values = dbController.getAllSTOEntities();


        Spinner spinnerTest = (Spinner) findViewById(R.id.author_spinner);

        ArrayAdapter<STO> authorAdapter = new ArrayAdapter<STO>(this,
                android.R.layout.simple_spinner_dropdown_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTest.setAdapter(authorAdapter);
        spinnerTest.setOnItemSelectedListener(this);

	}
	


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		//do nothing
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// do nothing

	}

}
