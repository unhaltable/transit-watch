package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.sf.nextbus.publicxmlfeed.domain.NextbusValueObject;
import net.sf.nextbus.publicxmlfeed.impl.NextbusService;

import java.util.List;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.adapters.NextbusListAdapter;

public abstract class SelectTransitItemFragment<T extends NextbusValueObject> extends Fragment implements AdapterView.OnItemClickListener {

    private ListView itemList;
    private NextbusListAdapter<T> listAdapter;

    protected static NextbusService nbs = StopsActivity.getNextbusService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getRootView(inflater, container);

        SelectStopActivity mParent = (SelectStopActivity) getActivity();

        // Change title, menu items
        //noinspection ConstantConditions
        mParent.getActionBar().setTitle(getTitleResId());
        setHasOptionsMenu(true);

        // Display the routes in the list
        setupList(rootView);

        // Set up the filter box
        EditText filter = (EditText) rootView.findViewById(R.id.filter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        return rootView;
    }

    private void setupList(View rootView) {
        itemList = (ListView) rootView.findViewById(android.R.id.list);

        new GetItems().execute();

        itemList.setClickable(true);
        itemList.setOnItemClickListener(this);
    }

    protected ListView getListView() {
        return itemList;
    }

    protected abstract View getRootView(LayoutInflater inflater, ViewGroup container);

    protected abstract int getTitleResId();

    /**
     * @return the list of NextBus items from the network
     */
    protected abstract List<T> getItemList();

    /**
     * @param items items to put in an adapter
     * @return a new adapter to use for the ListView
     */
    protected abstract NextbusListAdapter<T> getNewListAdapter(List<T> items);

    protected class GetItems extends AsyncTask<Void, Void, List<T>> {
        @Override
        protected List<T> doInBackground(Void... params) {
            List<T> itemList;
            try {
                itemList = getItemList();
            } catch (Exception e) {
                cancel(true);
                return null;
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<T> items) {
            listAdapter = getNewListAdapter(items);
            itemList.setAdapter(listAdapter);
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getActivity(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }
    }
}
