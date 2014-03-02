package ca.cryptr.transit_watch.receiver;

import android.content.Context;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.UUID;

import ca.cryptr.transit_watch.preferences.PreferencesDataSource;

public class DataReceiver extends PebbleKit.PebbleDataReceiver {

    private static final String TAG = DataReceiver.class.getName();

    public static final int MESSAGE_TYPE = 0;

    public static final long APP_OPENED = 1;
    public static final long STOP_SELECTED = 2;

    public static final int STOP_INDEX = 3;

    private static final UUID WATCHAPP_UUID = UUID.fromString("f407f26b-2d3c-4de5-b7fb-71c9eeaaf261");

    private PreferencesDataSource mPreferencesDataSource = null;

    public DataReceiver() {
        super(WATCHAPP_UUID);
    }

    @Override
    public void receiveData(Context context, int transactionId, PebbleDictionary data) {
        Log.i(TAG, "Received broadcast from Pebble watchapp.");

        if (mPreferencesDataSource == null) {
            mPreferencesDataSource = new PreferencesDataSource(context);
            mPreferencesDataSource.open();
        }

        long messageType = data.getInteger(MESSAGE_TYPE);
        if (messageType == APP_OPENED) {
            PebbleKit.sendAckToPebble(context, transactionId);
            onAppOpened(context);
        } else if (messageType == STOP_SELECTED) {
            PebbleKit.sendAckToPebble(context, transactionId);
            onStopSelected(context, data.getUnsignedInteger(STOP_INDEX));
        } else {
            PebbleKit.sendNackToPebble(context, transactionId);
        }
    }

    /**
     * Send saved stop data to watchapp. In the sent dictionary:
     *
     * 0: message type
     * 1: total number of stops
     * 2: number of data fields per stop
     * say the number of data fields per stop is n
     * 3 to n+3: first stop data
     * n+4 to 2n+4: second stop data
     * etc
     */
    private void onAppOpened(Context context) {
        // Sync saved ca.cryptr.transit_watch.stops
        PebbleDictionary savedStops = new PebbleDictionary();
        int stopCount = 1;
        int dictionaryIndex = 3;
        for (Direction direction : mPreferencesDataSource.getStops()) {
            Route route = direction.getRoute();

            String routeTag = route.getTag();
            String routeTitle = route.getShortTitle() != null ? route.getShortTitle() : route.getTitle();
            String directionName = direction.getName();

            for (Stop stop : direction.getStops()) {
                String stopTitle = stop.getShortTitle() != null ? stop.getShortTitle() : stop.getTitle();

                savedStops.addString(dictionaryIndex++, routeTag);
                savedStops.addString(dictionaryIndex++, routeTitle);
                savedStops.addString(dictionaryIndex++, directionName);
                savedStops.addString(dictionaryIndex++, stopTitle);

                stopCount++;
            }
        }

        savedStops.addUint32(0, 0); // 0 is MESSAGE_STOP_DATA in app.c
        savedStops.addUint32(1, stopCount);
        savedStops.addUint32(2, 4);  // Four fields

        Log.i(TAG, "Sending stop data: ");
        PebbleKit.sendDataToPebble(context, WATCHAPP_UUID, savedStops);

        // Test code:
        PebbleDictionary dictionary = new PebbleDictionary();
        dictionary.addString(0, "Hello");
        dictionary.addString(2, "Goodbye");
        dictionary.addString(1, "Blah");
//        PebbleKit.sendDataToPebble(context, WATCHAPP_UUID, dictionary);
    }

    private void onStopSelected(Context context, long stopIndex) {
        // Send vehicle predictions and weather

    }

}
