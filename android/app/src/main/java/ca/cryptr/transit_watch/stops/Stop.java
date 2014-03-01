package ca.cryptr.transit_watch.stops;

public class Stop {

    private String transit, route, stop;

    public Stop (String transit, String route, String stop) {
        this.transit = transit;
        this.route = route;
        this.stop = stop;
    }

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }
}
