package ca.cryptr.transit_watch.util;

import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

public class AndroidNextbusService extends NextbusService {

    public AndroidNextbusService() {
        super(new AndroidRPCImpl());
    }

}
