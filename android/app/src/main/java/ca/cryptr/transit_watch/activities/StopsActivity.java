package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

import java.io.IOException;
import java.util.ArrayList;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;
import ca.cryptr.transit_watch.stops.Stop;
import ca.cryptr.transit_watch.stops.StopListAdapter;
import ca.cryptr.transit_watch.util.AndroidNextbusService;
import ca.cryptr.transit_watch.weather.LocationChecker;

public class StopsActivity extends Activity {

//    private static final String TAG = StopsActivity.class.getSimpleName();
//
//    private NextbusService mNextbusService;
//    private PreferencesDataSource mPreferencesDataSource;

    private TextView city, temperature, summary;

    private ListView listStops;
    private StopListAdapter adapter;

    private ArrayList<Stop> favStops = new ArrayList<Stop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

//        mNextbusService = new AndroidNextbusService();
//        mPreferencesDataSource = new PreferencesDataSource(this);
//        mPreferencesDataSource.open();

        // Weather
        city = (TextView) findViewById(R.id.forecast_city);
        temperature = (TextView) findViewById(R.id.forecast_temperature);
        summary = (TextView) findViewById(R.id.forecast_summary);

        LocationChecker lc = new LocationChecker();
        try {
            String cityName = lc.getCity(this);
            city.setText(cityName);
        } catch (IOException e) {
            city.setText(R.string.location_error);
            summary.setText(R.string.location_error_check);
        }

        // Fav stops
        favStops.add(new Stop("TTC", "5 N Avenue Rd", "Queen's Park @ Museum Station"));
        favStops.add(new Stop("MiWay", "1 E Hurontario Rd", "Hurontario @ Eglinton"));
        favStops.add(new Stop("YRT", "56 S Highway 7", "Brantford @ Yonge"));
        setupFavStopsList();

        // TEMP
//        new AddStopTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stops, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addStopButton(View view) {
        Intent intent = new Intent(this, AddStopActivity.class);
        startActivity(intent);
    }

    protected Object mActionMode;
    public int selectedItem = -1;

    private void setupFavStopsList() {
        listStops = (ListView) findViewById(R.id.stops);

        // Display a message if there's no stops saved
        listStops.setEmptyView(findViewById(R.id.empty_list));

        adapter = new StopListAdapter(this, favStops);
        listStops.setAdapter(adapter);

        listStops.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                if (mActionMode != null)
                    return false;

                selectedItem = pos;

                // start the CAB using the ActionMode.Callback defined above
                mActionMode = StopsActivity.this.startActionMode(mActionModeCallback);
                view.setSelected(true);

                return true;
            }
        });
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove:
                    removeStop();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            selectedItem = -1;
        }
    };

    private void removeStop() {
        favStops.remove(selectedItem);
        adapter.notifyDataSetChanged();
//        Toast.makeText(this,
//                String.valueOf(selectedItem), Toast.LENGTH_LONG).show();
    }

    // TEMP
//    private class AddStopTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            Agency ttc = mNextbusService.getAgency("ttc");
//            Route carlton506 = null;
//            for (Route route : mNextbusService.getRoutes(ttc))
//                if (route.getTag().equals("506"))
//                    carlton506 = route;
//            Direction carlton506east = null;
//            for (Direction direction : mNextbusService.getRouteConfiguration(carlton506).getDirections())
//                if (direction.getName().equals("East"))
//                    carlton506east = direction;
//            Stop stGeorgeAndBeverly = null;
//            for (Stop stop : carlton506east.getStops())
//                if (stop.getTag().equals("2748"))
//                    stGeorgeAndBeverly = stop;
//            mPreferencesDataSource.insertStop(carlton506east, stGeorgeAndBeverly);
//            return null;
//        }
//    }

}
