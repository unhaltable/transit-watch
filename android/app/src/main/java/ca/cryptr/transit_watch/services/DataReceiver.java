package ca.cryptr.transit_watch.services;

import android.content.Context;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.UUID;

import ca.cryptr.transit_watch.preferences.PreferencesDataSource;
import ca.cryptr.transit_watch.preferences.RouteStopTuple;

public class DataReceiver extends PebbleKit.PebbleDataReceiver {

    public static final int MESSAGE_TYPE = 0;

    public static final long APP_OPENED = 1;
    public static final long STOP_SELECTED = 2;

    public static final int STOP_INDEX = 3;

    private static final UUID WATCHAPP_UUID = UUID.fromString("454d46ec-0647-4ef4-8df8-53e1e378cc48");

    private PreferencesDataSource mPreferencesDataSource = null;

    public DataReceiver() {
        super(WATCHAPP_UUID);
    }

    @Override
    public void receiveData(Context context, int transactionId, PebbleDictionary data) {
        if (mPreferencesDataSource == null) {
            mPreferencesDataSource = new PreferencesDataSource(context);
            mPreferencesDataSource.open();
        }

        long messageType = data.getInteger(MESSAGE_TYPE);
        if (messageType == APP_OPENED) {
            onAppOpened(context);
            PebbleKit.sendAckToPebble(context, transactionId);
        } else if (messageType == STOP_SELECTED) {
            onStopSelected(context, data.getUnsignedInteger(STOP_INDEX));
            PebbleKit.sendAckToPebble(context, transactionId);
        } else {
            PebbleKit.sendNackToPebble(context, transactionId);
        }
    }

    private void onAppOpened(Context context) {
        // Sync saved ca.cryptr.transit_watch.stops
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

        PebbleKit.sendDataToPebble(context, WATCHAPP_UUID, savedStops);
    }

    private void onStopSelected(Context context, long stopIndex) {
        // Send vehicle predictions

    }

}
