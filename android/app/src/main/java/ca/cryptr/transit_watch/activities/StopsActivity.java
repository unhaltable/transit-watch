package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;
import net.sf.nextbus.publicxmlfeed.impl.SimplestNextbusServiceAdapter;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.services.WatchListenerService;

public class StopsActivity extends Activity {

    private static final String TAG = StopsActivity.class.getSimpleName();

    private NextbusService mNextbusService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        mNextbusService = new SimplestNextbusServiceAdapter();

        // Start background service if not already started
        startService(new Intent(this, WatchListenerService.class));
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
                return true;
            case R.id.about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TEMP
    private void addStop() {
        Agency ttc = mNextbusService.getAgency("ttc");
        Route carlton506;
        for (Route route : mNextbusService.getRoutes(ttc))
            if (route.getTag() == "506")
                carlton506 = route;
        Direction
    }

}
