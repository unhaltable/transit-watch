package ca.cryptr.transit_watch.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Direction;

import java.util.List;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.adapters.DirectionsAdapter;
import ca.cryptr.transit_watch.adapters.NextbusListAdapter;

public class SelectDirectionFragment extends SelectTransitItemFragment<Direction> {

    private SelectStopActivity mParent;
    private TextView routeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mParent = (SelectStopActivity) getActivity();

        // Display route name
        routeName = (TextView) rootView.findViewById(R.id.direction_route_name);
        routeName.setText(mParent.getRoute().getTag());

        return rootView;
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_direction, container, false);
    }

    @Override
    protected int getTitleResId() {
        return R.string.title_activity_add_direction;
    }

    @Override
    protected List<Direction> getItemList() {
        assert nbs != null;
        assert mParent != null;
        assert nbs.getRouteConfiguration(mParent.getRoute()) != null;
        return nbs.getRouteConfiguration(mParent.getRoute()).getDirections();
    }

    @Override
    protected NextbusListAdapter<Direction> getNewListAdapter(List<Direction> items) {
        return new DirectionsAdapter(getActivity(), items);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Set the direction
        Direction d = (Direction) getListView().getItemAtPosition(position);
        mParent.setDirection(d);

        // Go to the next page
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SelectStopFragment nextStop = new SelectStopFragment();
        ft.replace(R.id.fragment_add, nextStop);
        ft.addToBackStack(null);
        ft.commit();
    }

}
