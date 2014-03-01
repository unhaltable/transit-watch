package ca.cryptr.transit_watch.cards;

import com.afollestad.cardsui.Card;

/**
 * Represents a single card displayed in a {@link ca.cryptr.transit_watch.adapters.RouteCardAdapter}.
 */
public class StopCard extends Card {

    private String route_name;
    private String stop_name;

    public StopCard(String route_name, String stop_name) {
        super(route_name, stop_name);

        this.route_name = route_name;
        this.stop_name = stop_name;
    }

    @Override
    public String getContent() {
        return stop_name;
    }

}
