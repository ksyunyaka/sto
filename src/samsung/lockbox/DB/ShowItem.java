package samsung.lockbox.DB;

import java.util.List;

import samsung.lockbox.control.Control;
import samsung.lockbox.model.Details;
import samsung.lockbox.utils.TimerLockSingleton;
import samsung.lockbox.utils.TimerLockUtil;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

public class ShowItem extends Activity {

	public static final int EDIT_ITEM = 701;

	private long categoryId;
	private long id;
	private Control control;

	private TableLayout tableLayout;
	private LinearLayout mainLayout;

	TextView itemName;

	private List<Details> details;
	/**
	 * This method start dial process
	 * 
	 * @author a.hontar
	 * @param telephonNumber
	 *            Calling number
	 * @throws Exception
	 */



	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(Menu.NONE, EDIT_ITEM, Menu.NONE, "Edit Item");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		TimerLockUtil timer=TimerLockSingleton.getInstance(this);
		timer.startTimer(this, false);
		switch (item.getItemId()) {
		case EDIT_ITEM: {
			Intent intent = new Intent();

			intent.putExtra("entityID", id);
			intent.putExtra("categoryId", categoryId);
			intent.setClass(this, EditItem.class);
			startActivityForResult(intent, EDIT_ITEM);
		}
			break;
		}
		return (super.onOptionsItemSelected(item));
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TimerLockUtil timer=TimerLockSingleton.getInstance(this);
		timer.startTimer(this, false);
		control = new Control(this);
		setContentView(R.layout.edit_item);
		
		ChangeBackground.setBackground("/sdcard/lockbox/option", this);		
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			id = bundle.getLong("entityID");
			categoryId = bundle.getLong("categoryId");
			// categoryId=1;

			control.open();
			itemName = new TextView(this);
			itemName.setText("Name: " + control.getEntity(id).getType());
			itemName.setTextSize(itemName.getTextSize() + 1);
			control.close();
			mainLayout = new LinearLayout(this);
			mainLayout.setOrientation(LinearLayout.VERTICAL);
			tableLayout = new TableLayout(this);

			mainLayout.addView(tableLayout);
			mainLayout.addView(itemName);
			createList();
		}
	}

	public void createList() {
		TimerLockUtil timer=TimerLockSingleton.getInstance(this);
		timer.startTimer(this, false);
		control.open();
		details = control.getDetailsByEntity(control.getEntity(id));
		// }
		control.close();
		tableLayout = new TableLayout(this);
		tableLayout = new TableLayout(this);
		tableLayout.setOrientation(TableLayout.VERTICAL);
		tableLayout.setColumnShrinkable(0, true);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		for (Details detail : details) {
			tableLayout.addView(detail.viewInViewActivity(this,
					metrics.widthPixels));
		}
		mainLayout.addView(tableLayout);

		ScrollView scrollPanel=new ScrollView(this);
		scrollPanel.addView(mainLayout);
		setContentView(scrollPanel);
	}


	public void dialNumber(String telephonNumber) {
		try {
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ telephonNumber)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open in browser this url-page
	 * 
	 * @author a.hontar
	 * @param url
	 *            Url page
	 */
	public void openBrowser(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://"+url));
		startActivity(intent);
	}

	/**
	 * Open image with Android's default image viewer
	 * 
	 * @author a.hontar
	 * @param uri
	 *            image address
	 */
	public void openImage(String uri) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + uri), "image/*");
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TimerLockUtil timer=TimerLockSingleton.getInstance(this);
		timer.startTimer(this, false);
		startActivity(getIntent());
		finish();
	};

}
