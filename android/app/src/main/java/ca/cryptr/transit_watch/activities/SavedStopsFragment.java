package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.cardsui.CardListView;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.adapters.RouteCardAdapter;
import ca.cryptr.transit_watch.cards.StopCard;

public class SavedStopsFragment extends Fragment {

    private CardListView mCardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stops, container, false);

        //noinspection ConstantConditions
        mCardList = (CardListView) rootView.findViewById(R.id.card_list);

        RouteCardAdapter cardAdapter = new RouteCardAdapter(getActivity(), R.layout.stop_card);
        cardAdapter.setAccentColorRes(R.color.ttc_red);

        cardAdapter.add(new StopCard("506 Carlton", "College St at Beverly St"));
        cardAdapter.add(new StopCard("54 Lawrence E", "Eglinton Ave at Redpath Ave"));

        mCardList.setAdapter(cardAdapter);

        return rootView;
    }

}
