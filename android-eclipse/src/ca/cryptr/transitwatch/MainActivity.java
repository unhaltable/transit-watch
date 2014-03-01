package ca.cryptr.transitwatch;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import transit.FileIO;
import transit.Stops;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private FileIO data;
	public static final String FILENAME = "/data.json";
	private static String FILEPATH;

	private SimpleAdapter adapter;
	private static ListView stopsListView;

	private TextView city, temperature, summary;

	private static final Stops stops = Stops.getInstance();

	protected Object mActionMode;
	public int selectedItem = -1;

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

		city = (TextView) findViewById(R.id.forecast_city);
		temperature = (TextView) findViewById(R.id.forecast_temperature);
		summary = (TextView) findViewById(R.id.forecast_summary);

		setupStopsList();

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		String locationProvider = LocationManager.NETWORK_PROVIDER;
		// Or use LocationManager.GPS_PROVIDER

		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

		if (lastKnownLocation != null) {
			Geocoder gcd = new Geocoder(this, Locale.getDefault());
			List<Address> addresses;
			try {
				addresses = gcd.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
				if (addresses.size() > 0)
					city.setText(addresses.get(0).getLocality());

				try {
					getCityCode(addresses.get(0).getLocality());
				} catch (NotFoundException e) {
					System.out.println("Fail1");
				}

			} catch (IOException e) {
				city.setText(R.string.location_error);
				summary.setText(R.string.location_error_check);
			}
		} else {
			city.setText(R.string.location_error);
			summary.setText(R.string.location_error_check);
		}

		stopsListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (mActionMode != null) {
					return false;
				}
				selectedItem = position;

				// start the CAB using the ActionMode.Callback defined above
				mActionMode = MainActivity.this
						.startActionMode((Callback) mActionMode);
				view.setSelected(true);
				return true;
			}
		});
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			// assumes that you have "contexual.xml" menu resources
			//			inflater.inflate(R.menu.rowselection, menu);
			return true;
		}

		// the following method is called each time
		// the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			//			case R.id.menuitem1_show:
			//				show();
			//				// the Action was executed, close the CAB
			//				mode.finish();
			//				return true;
			default:
				return false;
			}
		}

		// called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			selectedItem = -1;
		}
	};

	private void show() {
		Toast.makeText(MainActivity.this,
				String.valueOf(selectedItem), Toast.LENGTH_LONG).show();
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

	private void getCityCode(String city) throws IOException {
		//		InputStream is = this.getAssets().open("cities.txt");
		//		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		//		String line = reader.readLine();
		//		Map <String, City> cities = new HashMap<String, City>();
		//		int i = 0;
		//		City c = null;
		//		while (line != null) {
		//			if (i == 0) {
		//				c = new City();
		//				c.setCode(line);
		//			} else if (i == 1) {
		//				c.setNameEn(line);
		//			} else if (i == 2) {
		//				c.setNameFr(line);
		//			} else if (i == 3) {
		//				c.setProvince(line);
		//				cities.put(c.getNameEn(), c);
		//			} else if (i == 4) {
		//				i = 0;
		//			}
		//
		//			i++;
		//		}
		//
		//
		//		File file = new File("temp");
		//		FileOutputStream f = new FileOutputStream(file);
		//		ObjectOutputStream s = new ObjectOutputStream(f);
		//		s.writeObject(cities);
		//		s.close();
	}

	private void setupStopsList() {
		stopsListView = (ListView) findViewById(R.id.stops);

		// Display a message if there's no stops saved
		stopsListView.setEmptyView(findViewById(R.id.empty_list));

		// Make ListView items open an delete dialog
		stopsListView.setClickable(true);
		stopsListView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//						Dialog register = new Dialog(this);
						//						register.setTitle("Delete?");
						//
						//						Button dialogButton = (Button)register.findViewById(
						//								R.id.register_button);
						//						dialogButton.setOnClickListener(new View.OnClickListener() {
						//							@Override
						//							public void onClick(View v) {
						//								// Get the textbox values
						//								final String strUser, strPass, strPass2, strAdmin;
						//								strUser = dUser.getText().toString();
						//								strPass = dPass.getText().toString();
						//								strPass2 = dPass2.getText().toString();
						//								strAdmin = dAdmin.getText().toString();
						//								// Call register method to deal with the values
						//								registerCheck(strUser, strPass, strPass2, strAdmin, dMsg);
						//							}
						//						});
						//
						//						register.show();

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
		//		Collection<Stop> stops_list = stops.getStops().values();
		//		populateTransitList(stops_list.toArray(new String[stops_list.size()]));
	}


	private void populateTransitList(String[] stops_list) {
		StopAdapter adapter = new StopAdapter(this, stops_list);
		stopsListView.setAdapter(adapter);
	}

}
