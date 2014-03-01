package ca.cryptr.transitwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

}
