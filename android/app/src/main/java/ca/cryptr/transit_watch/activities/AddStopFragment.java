package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.content.Intent;
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

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.stops.Stop;

public class AddStopFragment extends Fragment {

    private ListView stops;
    private EditText filter;
    private SimpleAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_stop, container, false);

        // Change title, menu items
        getActivity().getActionBar().setTitle(R.string.title_activity_add_stop);
        setHasOptionsMenu(true);

        // Get the ca.cryptr.transit_watch.stops from the selected route

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
        menu.findItem(R.id.action_add_previous_route).setVisible(true);
        menu.findItem(R.id.action_add_done).setVisible(true);
    }

    private void setupStopsList() {
        stops = (ListView) view.findViewById(R.id.add_stop_list);

//        final String[] companies = getResources().getStringArray(R.array.transits);

        stops.setClickable(true);
        stops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                AddStopActivity stopActivity = (AddStopActivity) getActivity();
                // Set the stop name
//                stopActivity.setStop(companies[2 * (int) parent.getItemIdAtPosition(pos)]);

                // Create the Stop
                Stop stop = new Stop(stopActivity.getTransit(), stopActivity.getRoute(), stopActivity.getStop());
//                ca.cryptr.transit_watch.stops.add(stop);

                // Return to the main screen
                Intent intent = new Intent(getActivity(), StopsActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

//        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//
//        for (int i = 0; i < companies.length; i+=2) {
//            Map<String, String> item = new HashMap<String, String>();
//            item.put("1", companies[i]);
//            item.put("2", companies[i+1]);
//            data.add(item);
//        }
//
//        adapter = new SimpleAdapter(getActivity(), data,
//                R.layout.add_list_item,
//                new String[] {"1", "2"},
//                new int[] {R.id.text1, R.id.text2});

        stops.setAdapter(adapter);
    }
}
