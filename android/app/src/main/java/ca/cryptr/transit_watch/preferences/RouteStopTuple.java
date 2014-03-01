package ca.cryptr.transit_watch.preferences;

import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

public class RouteStopTuple {

    private Route mRoute;
    private Stop mStop;

    public RouteStopTuple(Route route, Stop stop) {
        mRoute = mRoute;
        mStop = mStop;
    }

    public Route getRoute() {
        return mRoute;
    }

    public Stop getStop() {
        return mStop;
    }
}
