package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cryptr.transit_watch.R;

public class AddRouteFragment extends Fragment {

    private ListView routes;
    private EditText filter;
    private TextView transitName;
    private SimpleAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_route, container, false);

        // Change title, menu items
        getActivity().getActionBar().setTitle(R.string.title_activity_add_route);
        setHasOptionsMenu(true);

        // Display transit name
        transitName = (TextView) view.findViewById(R.id.stop_transit_name);
        transitName.setText(((AddStopActivity) getActivity()).getTransit());

        // Get the routes from the selected transit

        // Display the routes in the list
        setupRoutesList();

        // Set up the filter box
        filter = (EditText) view.findViewById(R.id.filter_route);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_add_cancel).setVisible(false);
        menu.findItem(R.id.action_add_previous_transit).setVisible(true);
        menu.findItem(R.id.action_add_previous_route).setVisible(false);
        menu.findItem(R.id.action_add_done).setVisible(false);
    }

    private void setupRoutesList() {
        routes = (ListView) view.findViewById(R.id.add_route_list);

//        final String[] companies = getResources().getStringArray(R.array.transits);

        routes.setClickable(true);
        routes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                // Set the route name
//                ((AddStopActivity) getActivity()).setRoute(companies[2 * (int) parent.getItemIdAtPosition(pos)]);

                // Go to the next page
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddStopFragment nextStop = new AddStopFragment();
                ft.replace(R.id.fragment_add, nextStop);
                ft.addToBackStack(null);
                ft.commit();
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

        routes.setAdapter(adapter);
    }
}
