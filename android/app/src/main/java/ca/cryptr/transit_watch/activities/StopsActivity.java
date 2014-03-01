package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;
import ca.cryptr.transit_watch.util.AndroidNextbusService;

public class StopsActivity extends Activity {

    private static final String TAG = StopsActivity.class.getSimpleName();

    private NextbusService mNextbusService;
    private PreferencesDataSource mPreferencesDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        mNextbusService = new AndroidNextbusService();
        mPreferencesDataSource = new PreferencesDataSource(this);


        // TEMP
        new AddStopTask().execute();
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
//        Intent intent = new Intent(this, AddTransitActivity.class);
//        startActivity(intent);
    }

    // TEMP
    private class AddStopTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Agency ttc = mNextbusService.getAgency("ttc");
            Route carlton506 = null;
            for (Route route : mNextbusService.getRoutes(ttc))
                if (route.getTag().equals("506"))
                    carlton506 = route;
            Direction carlton506east = null;
            for (Direction direction : mNextbusService.getRouteConfiguration(carlton506).getDirections())
                if (direction.getName().equals("East"))
                    carlton506east = direction;
            Stop stGeorgeAndBeverly = null;
            for (Stop stop : carlton506east.getStops())
                if (stop.getTag().equals("2748"))
                    stGeorgeAndBeverly = stop;
            mPreferencesDataSource.saveStop(carlton506east, stGeorgeAndBeverly);
            return null;
        }
    }

}
