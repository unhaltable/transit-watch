package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;
import ca.cryptr.transit_watch.stops.FavStop;
import ca.cryptr.transit_watch.stops.StopListAdapter;
import ca.cryptr.transit_watch.util.AndroidNextbusService;
import ca.cryptr.transit_watch.util.FileIO;
import ca.cryptr.transit_watch.weather.LocationChecker;
import ca.cryptr.transit_watch.weather.SiteListParser;
import ca.cryptr.transit_watch.weather.Weather;

public class StopsActivity extends Activity {

    private int LIMIT = 10;

    private static FileIO data;
    public static final String FILENAME = "/data.json";
    private static String FILEPATH;

    private static final String TAG = StopsActivity.class.getSimpleName();

    private static Weather weather;
    private TextView city, temperature, summary;

    private static NextbusService mNextbusService;
    private PreferencesDataSource mPreferencesDataSource;

    private ListView listStops;
    private StopListAdapter adapter;
    private List<Stop> stops;

    private static ArrayList<FavStop> favStops;

    protected Object mActionMode;
    public int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        // Don't allow user to try to add stops if there's no Internet
        if (!isNetworkAvailable()) {
            findViewById(R.id.empty_add).setVisibility(View.GONE);
            findViewById(R.id.add_stop).setVisibility(View.GONE);
        }

        stops = new ArrayList<Stop>();
        favStops = new ArrayList<FavStop>();

        mNextbusService = new AndroidNextbusService();
        mPreferencesDataSource = new PreferencesDataSource(this);
        mPreferencesDataSource.open();

//        // Load saved data
//        FILEPATH = new File(this.getFilesDir(), FILENAME).getPath();
//        try {
//            data = new FileIO(this.getFilesDir(), FILENAME);
//            data.save(FILEPATH);
//        } catch (IOException e) {
//            Toast.makeText(this, R.string.file_error, Toast.LENGTH_SHORT).show();
//        }

        // Fav stops
        setupFavStopsList();

        // Weather
        weather = new Weather();
        setupWeather();
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

    @Override
    protected void onResume() {
        super.onResume();
        setupFavStopsList();
    }

    /**
     * Checks if there's an Internet connection.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Weather getWeather() {
        return weather;
    }

    public static NextbusService getNextbusService() {
        return mNextbusService;
    }

    public static ArrayList<FavStop> getFavStops() {
        return favStops;
    }

    public static void setFavStops(ArrayList<FavStop> favStops) {
        StopsActivity.favStops = favStops;
    }

    public static void addFavStop(FavStop stop) {
        favStops.add(stop);

        try {
            data.save(FILEPATH);
        } catch (IOException e) {

        }
    }

    private void removeStop() {
        mPreferencesDataSource.deleteStop(stops.get(selectedItem));
        setupFavStopsList();

        try {
            data.save(FILEPATH);
        } catch (IOException e) {
            Toast.makeText(this,
                    R.string.file_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void setupWeather() {
        // Weather
        city = (TextView) findViewById(R.id.forecast_city);
        temperature = (TextView) findViewById(R.id.forecast_temperature);
        summary = (TextView) findViewById(R.id.forecast_summary);

        try {
            LocationChecker lc = new LocationChecker();
            getWeather().setCityName(lc.getCity(this));
            new SiteListParser(city, temperature, summary).execute();
        } catch (IOException e) {
            city.setText(R.string.location_error);
            summary.setText(R.string.location_error_check);
        }
    }

    public void addStopButton(View view) {
        Intent intent = new Intent(this, SelectStopActivity.class);
        startActivity(intent);
    }

    private void setupFavStopsList() {
        listStops = (ListView) findViewById(R.id.stops);

        // Display a message if there's no stops saved
        listStops.setEmptyView(findViewById(R.id.empty_list));

        for (Direction d : mPreferencesDataSource.getStops())
            for (Stop s : d.getStops()) {
                stops.add(s);
                favStops.add(new FavStop(s.getAgency().getTitle(),
                                     (d.getRoute().getShortTitle() != null ? d.getRoute().getShortTitle() : d.getRoute().getTitle()),
                                     (s.getShortTitle()) != null ? s.getShortTitle() : s.getTitle()));
            }

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
}
