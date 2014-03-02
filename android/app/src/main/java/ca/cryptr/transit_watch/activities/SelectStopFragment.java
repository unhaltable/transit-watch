package ca.cryptr.transit_watch.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.List;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.adapters.NextbusListAdapter;
import ca.cryptr.transit_watch.adapters.StopsAdapter;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;

public class SelectStopFragment extends SelectTransitItemFragment<Stop> {

    private SelectStopActivity mParent;
    private TextView directionName;
    private PreferencesDataSource mPreferencesDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPreferencesDataSource = new PreferencesDataSource(getActivity());
        mPreferencesDataSource.open();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mParent = (SelectStopActivity) getActivity();

        // Display route name
        directionName = (TextView) rootView.findViewById(R.id.stop_route_name);
        directionName.setText(mParent.getDirection().getTag());

        return rootView;
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_stop, container, false);
    }

    @Override
    protected int getTitleResId() {
        return R.string.title_activity_add_stop;
    }

    @Override
    protected List<Stop> getItemList() {
        return mParent.getDirection().getStops();
    }

    @Override
    protected NextbusListAdapter<Stop> getNewListAdapter(List<Stop> items) {
        return new StopsAdapter(getActivity(), items);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_add_cancel).setVisible(false);
        menu.findItem(R.id.action_add_previous_transit).setVisible(false);
        menu.findItem(R.id.action_add_previous_route).setVisible(false);
        menu.findItem(R.id.action_add_previous_dir).setVisible(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Stop s = (Stop) getListView().getItemAtPosition(position);

        mPreferencesDataSource.saveStop(mParent.getDirection(), s);

        // Return to the main screen
        getActivity().finish();
    }
}
