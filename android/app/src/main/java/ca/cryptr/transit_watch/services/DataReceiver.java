package ca.cryptr.transit_watch.services;

import android.content.Context;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

public class DataReceiver extends PebbleKit.PebbleDataReceiver {

    public static final int MESSAGE_TYPE = 0;

    public static final long APP_OPENED = 1;
    public static final long STOP_SELECTED = 2;

    public static final int STOP_INDEX = 3;

    private Callbacks callbacks;

    protected DataReceiver(UUID subscribedUuid, Callbacks callbacks) {
        super(subscribedUuid);
        this.callbacks = callbacks;
    }

    @Override
    public void receiveData(Context context, int transactionId, PebbleDictionary data) {
        long messageType = data.getInteger(MESSAGE_TYPE);
        if (messageType == APP_OPENED) {
            callbacks.onAppOpened();
            PebbleKit.sendAckToPebble(context, transactionId);
        } else if (messageType == STOP_SELECTED) {
            callbacks.onStopSelected(data.getUnsignedInteger(STOP_INDEX));
            PebbleKit.sendAckToPebble(context, transactionId);
        } else {
            PebbleKit.sendNackToPebble(context, transactionId);
        }
    }

    protected static interface Callbacks {

        public void onAppOpened();
        public void onStopSelected(long stopIndex);

    }

}
