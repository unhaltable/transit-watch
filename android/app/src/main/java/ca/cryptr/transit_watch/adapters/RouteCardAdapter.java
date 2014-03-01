package ca.cryptr.transit_watch.adapters;

import android.content.Context;
import android.view.View;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;

/**
 * A {@link com.afollestad.cardsui.CardAdapter} that displays {@link ca.cryptr.transit_watch.cards.StopCard} and
 * {@link com.afollestad.cardsui.CardHeader} objects in a {@link com.afollestad.cardsui.CardListView}.
 */
public class RouteCardAdapter extends CardAdapter<Card> {

    /**
     * Initializes a new CardAdapter instance.
     *
     * @param context       The context used to inflate layouts and retrieve resources.
     * @param cardLayoutRes Sets a custom layout to be used for all cards (not including headers) in the adapter.
     */
    public RouteCardAdapter(Context context, int cardLayoutRes) {
        super(context, cardLayoutRes);
    }

    @Override
    public View onViewCreated(int index, View recycled, Card item) {
//        TextView description = (TextView) recycled.findViewById(R.id.description);
//        if (description != null) onProcessContent(description, (StopCard) item);

        return super.onViewCreated(index, recycled, item);
    }

//    @Override
//    protected boolean onProcessTitle(TextView title, Card card, int accentColor) {
//        // Ignore accentColor; just use the title's current color
//        return super.onProcessTitle(title, card, title.getCurrentTextColor());
//    }

}