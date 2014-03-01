package ca.cryptr.transitwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AddTransitActivity extends Activity {

	private SimpleAdapter adapter;
	private ListView transits;
	private EditText filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_transit);
		setupActionBar();

		setupTransitList();

		filter = (EditText) findViewById(R.id.filter_transit);
		filter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				AddTransitActivity.this.adapter.getFilter().filter(s);
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

		});
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_transit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_transit_next:
			Intent intent = new Intent(this, AddRouteActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupTransitList() {
		transits = (ListView) findViewById(R.id.add_transit_list);

		String[] companies = getResources().getStringArray(R.array.transits);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		for (int i = 0; i < companies.length; i+=2) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("1", companies[i]);
			item.put("2", companies[i+1]);
			data.add(item);
		}

		adapter = new SimpleAdapter(this, data,
				R.layout.add_list_item,
				new String[] {"1", "2"},
				new int[] {R.id.text1, R.id.text2});

		transits.setAdapter(adapter);
	}

}
