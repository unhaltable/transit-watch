package ca.cryptr.transit_watch.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;

import java.util.List;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.adapters.NextbusListAdapter;
import ca.cryptr.transit_watch.adapters.RoutesAdapter;

public class SelectRouteFragment extends SelectTransitItemFragment<Route> {

    private static final String TAG = SelectRouteFragment.class.getSimpleName();

    private SelectStopActivity mParent;
    private TextView transitAgencyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mParent = (SelectStopActivity) getActivity();

        // Display transit name
        transitAgencyName = (TextView) rootView.findViewById(R.id.stop_transit_name);
        transitAgencyName.setText(((SelectStopActivity) getActivity()).getAgency().getTitle());

        return rootView;
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_route, container, false);
    }

    @Override
    protected int getTitleResId() {
        return R.string.title_activity_add_route;
    }

    @Override
    protected List<Route> getItemList() {
        Agency agency = ((SelectStopActivity) getActivity()).getAgency();

        try {
            Log.i(TAG, "Loading routes for " + agency.getTitle());
            return nbs.getRoutes(agency);
        } catch (TransientServiceException e) {
            return null;
        }
    }

    @Override
    protected NextbusListAdapter<Route> getNewListAdapter(List<Route> items) {
        return new RoutesAdapter(getActivity(), items);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_add_cancel).setVisible(false);
        menu.findItem(R.id.action_add_previous_transit).setVisible(true);
        menu.findItem(R.id.action_add_previous_route).setVisible(false);
        menu.findItem(R.id.action_add_previous_dir).setVisible(false);
        menu.findItem(R.id.action_add_done).setVisible(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Set the route name
        Route r = (Route) getListView().getItemAtPosition(position);
        mParent.setRoute(r);

        // Go to the next page
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SelectDirectionFragment nextDir = new SelectDirectionFragment();
        ft.replace(R.id.fragment_add, nextDir);
        ft.addToBackStack(null);
        ft.commit();
    }

}
