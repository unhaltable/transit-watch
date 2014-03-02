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

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cryptr.transit_watch.R;

public class AddDirectionFragment extends Fragment {

    private ListView dirList;
    private EditText filter;
    private TextView routeName;
    private SimpleAdapter adapter;
    private View view;

    private static NextbusService nbs = StopsActivity.getmNextbusService();

    private List<Direction> directions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_direction, container, false);

        // Change title, menu items
        getActivity().getActionBar().setTitle(R.string.title_activity_add_direction);
        setHasOptionsMenu(true);

        // Display transit name
        routeName = (TextView) view.findViewById(R.id.direction_route_name);
        routeName.setText(AddStopActivity.getRoute());

        // Display the routes in the list
        setupDirectionList();

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
        menu.findItem(R.id.action_add_previous_transit).setVisible(false);
        menu.findItem(R.id.action_add_previous_route).setVisible(true);
        menu.findItem(R.id.action_add_previous_dir).setVisible(false);
        menu.findItem(R.id.action_add_done).setVisible(false);
    }

    private void setupDirectionList() {
        dirList = (ListView) view.findViewById(R.id.add_route_list);

        (new GetDirections()).execute();

        dirList.setClickable(true);
        dirList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                // Set the direction
                Direction d = directions.get((int) parent.getItemIdAtPosition(pos));
                AddStopActivity.setDirTag(d.getTitle());
                AddStopActivity.setDirObj(d);

                // Go to the next page
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddStopFragment nextStop = new AddStopFragment();
                ft.replace(R.id.fragment_add, nextStop);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private class GetDirections extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            directions = nbs.getRouteConfiguration(AddStopActivity.getRouteObj()).getDirections();
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            for (Direction d : directions) {
                Map<String, String> item = new HashMap<String, String>();

                item.put("1", d.getTitle());
                item.put("2", d.getName());
                data.add(item);
            }

            adapter = new SimpleAdapter(getActivity(), data,
                    R.layout.add_list_item,
                    new String[] {"1", "2"},
                    new int[] {R.id.text1, R.id.text2});

            dirList.setAdapter(adapter);
        }
    }
}
