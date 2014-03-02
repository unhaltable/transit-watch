package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cryptr.transit_watch.R;

public class AddRouteFragment extends Fragment {

    private ListView routesList;
    private EditText filter;
    private TextView transitName;
    private SimpleAdapter adapter;
    private View view;

    private static NextbusService nbs = StopsActivity.getmNextbusService();

    private List<Route> routes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_route, container, false);

        // Change title, menu items
        getActivity().getActionBar().setTitle(R.string.title_activity_add_route);
        setHasOptionsMenu(true);

        // Display transit name
        transitName = (TextView) view.findViewById(R.id.stop_transit_name);
        transitName.setText(((AddStopActivity) getActivity()).getAgency());

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
        menu.findItem(R.id.action_add_previous_dir).setVisible(false);
        menu.findItem(R.id.action_add_done).setVisible(false);
    }

    private void setupRoutesList() {
        routesList = (ListView) view.findViewById(R.id.add_route_list);

        (new GetRoutes()).execute();

        routesList.setClickable(true);
        routesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                // Set the route name
                Route r = routes.get((int) parent.getItemIdAtPosition(pos));
                AddStopActivity.setRoute(r.getTitle());
                AddStopActivity.setRouteTag(r.getTag());
                AddStopActivity.setRouteObj(r);

                // Go to the next page
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddDirectionFragment nextDir = new AddDirectionFragment();
                ft.replace(R.id.fragment_add, nextDir);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private class GetRoutes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                routes = nbs.getRoutes(nbs.getAgency(AddStopActivity.getAgencyTag()));
            } catch (TransientServiceException e) {
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            for (Route r : routes) {
                Map<String, String> item = new HashMap<String, String>();

                item.put("1", r.getTitle());
                item.put("2", r.getTag());
                data.add(item);
            }

            adapter = new SimpleAdapter(getActivity(), data,
                    R.layout.add_list_item,
                    new String[] {"1", "2"},
                    new int[] {R.id.text1, R.id.text2});

            routesList.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getActivity(), "Could not retrieve routes.", Toast.LENGTH_SHORT).show();
            // Go to previous page
            getFragmentManager().popBackStack();
        }

    }
}
