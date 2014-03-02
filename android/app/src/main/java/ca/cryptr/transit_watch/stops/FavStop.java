package ca.cryptr.transit_watch.stops;

public class FavStop {

    private String agency, route, stop;

    public FavStop(String agency, String route, String stop) {
        this.agency = agency;
        this.route = route;
        this.stop = stop;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
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
