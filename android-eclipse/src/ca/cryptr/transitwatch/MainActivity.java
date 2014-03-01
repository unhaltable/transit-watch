package ca.cryptr.transitwatch;

import java.io.File;
import java.io.IOException;

import transit.FileIO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private FileIO data;
	public static final String FILENAME = "/data.json";
	private static String FILEPATH;

	private static ListView stopsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FILEPATH = new File(this.getApplicationContext().getFilesDir(),
				FILENAME).getPath();

		try {
			data = new FileIO(
					this.getApplicationContext().getFilesDir(),
					FILENAME);
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					R.string.file_error, Toast.LENGTH_SHORT).show();
		}

		setupStopsList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			showActivity(SettingsActivity.class);
			return true;
		default:
			return false;
		}
	}

	public void addStop(View view) {
		showActivity(AddTransitActivity.class);
	}

	/**
	 * Starts and displays the specified activity.
	 * @param activity The activity to display.
	 */
	public void showActivity(Class<?> activity) {
		Intent intent = new Intent(this, activity);
		startActivity(intent);
	}

	private void setupStopsList() {
		stopsListView = (ListView)findViewById(R.id.stops);

		// Display a message if there's no stops saved
		stopsListView.setEmptyView(findViewById(R.id.empty_list));

		// Make ListView items open an delete dialog
		stopsListView.setClickable(true);
		stopsListView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//						Intent patientIntent = new Intent(MainActivity.this,
						//								PatientActivity.class);
						//						Patient p = patientsList.get(
						//								(int) parent.getItemIdAtPosition(
						//										position));
						//						patientIntent.putExtra("patient", p.getHealthCard());
						//						startActivity(patientIntent);
					}
				});

		// Populate the ListView with all stops
		//populateList(er.getPatients(sortArrival, sortUnseen));
	}

}
