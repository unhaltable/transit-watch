package ca.cryptr.transit_watch.services;

import android.app.IntentService;
import android.content.Intent;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

import ca.cryptr.transit_watch.R;

public class WatchListenerService extends IntentService implements DataReceiver.Callbacks {

    private PebbleKit.PebbleDataReceiver mDataReceiver = null;
    private UUID watchappUUID;

    public WatchListenerService(String name) {
        super(name);
        this.watchappUUID = UUID.fromString(getString(R.string.watchapp_uuid));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mDataReceiver == null) {
            mDataReceiver = new DataReceiver(UUID.fromString(getString(R.string.watchapp_uuid)), this);
            PebbleKit.registerReceivedDataHandler(this, mDataReceiver);
        }
    }

    @Override
    public void onAppOpened() {
        // Send saved stops to pebble


        PebbleDictionary savedStops = new PebbleDictionary();


//        PebbleKit.sendDataToPebble(this, );
    }

    @Override
    public void onStopSelected(long stopIndex) {
        // Send vehicle predictions
    }
}
