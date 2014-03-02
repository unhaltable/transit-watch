package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.stops.FavStop;

public class AddStopFragment extends Fragment {

    private ListView stopsList;
    private EditText filter;
    private TextView routeDirName;
    private SimpleAdapter adapter;
    private View view;

    private static NextbusService nbs = StopsActivity.getmNextbusService();

    List<Stop> stops;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_stop, container, false);

        // Change title, menu items
        getActivity().getActionBar().setTitle(R.string.title_activity_add_stop);
        setHasOptionsMenu(true);

        // Display transit name
        routeDirName = (TextView) view.findViewById(R.id.stop_route_name);
        routeDirName.setText(AddStopActivity.getRoute());

        // Display the ca.cryptr.transit_watch.stops in the list
        setupStopsList();

        // Set up the filter box
        filter = (EditText) view.findViewById(R.id.filter_stop);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_add_cancel).setVisible(false);
        menu.findItem(R.id.action_add_previous_transit).setVisible(false);
        menu.findItem(R.id.action_add_previous_route).setVisible(false);
        menu.findItem(R.id.action_add_previous_dir).setVisible(true);
        menu.findItem(R.id.action_add_done).setVisible(true);
    }

    private void setupStopsList() {
        stopsList = (ListView) view.findViewById(R.id.add_stop_list);

        (new GetStops()).execute();

        stopsList.setClickable(true);
        stopsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                AddStopActivity asa = (AddStopActivity) getActivity();
                Stop s = stops.get((int) parent.getItemIdAtPosition(pos));
                // Set the stop name
                asa.setStop(s.getTitle());

                // Create the Stop
                FavStop stop = new FavStop(asa.getAgency(), asa.getRoute(), asa.getStop());
                StopsActivity.addFavStop(stop);

                // Return to the main screen
                getActivity().finish();
            }
        });
    }

    private class GetStops extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            stops = AddStopActivity.getDirObj().getStops();
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            for (Stop s : stops) {
                Map<String, String> item = new HashMap<String, String>();

                item.put("1", s.getTitle());
                item.put("2", s.getTag() + ", " + s.getGeolocation());
                data.add(item);
            }

            adapter = new SimpleAdapter(getActivity(), data,
                    R.layout.add_list_item,
                    new String[] {"1", "2"},
                    new int[] {R.id.text1, R.id.text2});

            stopsList.setAdapter(adapter);
        }
    }
}
