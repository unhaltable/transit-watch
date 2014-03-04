package ca.cryptr.transit_watch.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Agency;

import java.util.List;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.adapters.AgenciesAdapter;
import ca.cryptr.transit_watch.adapters.NextbusListAdapter;

public class SelectAgencyFragment extends SelectTransitItemFragment<Agency> {

    private static final String TAG = SelectAgencyFragment.class.getSimpleName();

    private SelectStopActivity mParent;
    private TextView transitAgencyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mParent = (SelectStopActivity) getActivity();

        return rootView;
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_transit, container, false);
    }

    @Override
    protected int getTitleResId() {
        return R.string.title_activity_add_transit;
    }

    @Override
    protected List<Agency> getItemList() {
        return nbs.getAgencies();
    }

    @Override
    protected NextbusListAdapter<Agency> getNewListAdapter(List<Agency> items) {
        return new AgenciesAdapter(getActivity(), items);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Set the transit name
        Agency a = (Agency) getListView().getItemAtPosition(position);
        ((SelectStopActivity) getActivity()).setAgency(a);

        Log.i(TAG, "Set agency in SelectStopActivity.");

        // Go to the next page
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SelectRouteFragment nextRoute = new SelectRouteFragment();
        ft.replace(R.id.fragment_add, nextRoute);
        ft.addToBackStack(null);
        ft.commit();
    }
}
