package ca.cryptr.transit_watch.services;

import android.app.IntentService;
import android.content.Intent;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.UUID;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;
import ca.cryptr.transit_watch.preferences.RouteStopTuple;

public class WatchListenerService extends IntentService implements DataReceiver.Callbacks {

    private static final String PREFERENCES_NAME = "transit_stops";

    private PebbleKit.PebbleDataReceiver mDataReceiver = null;
    private UUID mWatchappUUID;
    private PreferencesDataSource mPreferencesDataSource;

    public WatchListenerService(String name) {
        super(name);
        mWatchappUUID = UUID.fromString(getString(R.string.watchapp_uuid));
        mPreferencesDataSource = new PreferencesDataSource(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mDataReceiver == null) {
            mDataReceiver = new DataReceiver(mWatchappUUID, this);
            PebbleKit.registerReceivedDataHandler(this, mDataReceiver);
        }
    }

    @Override
    public void onAppOpened() {
        // Send saved stops to pebble
        PebbleDictionary savedStops = new PebbleDictionary();
        for (RouteStopTuple routeStop : mPreferencesDataSource.getStops()) {
            Route route = routeStop.getRoute();
            Stop stop = routeStop.getStop();

            String routeTag = route.getTag();
            String routeTitle = route.getShortTitle() != null ? route.getShortTitle() : route.getTitle();
            String stopTitle = stop.getShortTitle() != null ? stop.getShortTitle() : stop.getTitle();

            // We can only send primitive data to Pebble
            // TODO: figure out in what format to send the data
            
        }

        PebbleKit.sendDataToPebble(this, mWatchappUUID, savedStops);
    }

    @Override
    public void onStopSelected(long stopIndex) {
        // Send vehicle predictions
    }
}
