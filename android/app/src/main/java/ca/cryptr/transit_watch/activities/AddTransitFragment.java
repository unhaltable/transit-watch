package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ca.cryptr.transit_watch.R;

public class AddTransitFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_transit, container, false);

        // Change title, menu items
        getActivity().getActionBar().setTitle(R.string.title_activity_add_transit);
        setHasOptionsMenu(true);

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_add_cancel).setVisible(true);
        menu.findItem(R.id.action_add_next_route).setVisible(true);
        menu.findItem(R.id.action_add_previous_transit).setVisible(false);
        menu.findItem(R.id.action_add_next_stop).setVisible(false);
        menu.findItem(R.id.action_add_previous_route).setVisible(false);
        menu.findItem(R.id.action_add_done).setVisible(false);
    }

}
