package ca.cryptr.transit_watch.stops;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ca.cryptr.transit_watch.R;

public class StopListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Stop> data;
    private static LayoutInflater inflater = null;

    public StopListAdapter(Activity a, ArrayList<Stop> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.stop_list_item, null);

        TextView minutes = (TextView)vi.findViewById(R.id.minutes);
        TextView route = (TextView)vi.findViewById(R.id.route);
        TextView stop = (TextView)vi.findViewById(R.id.stop);

        Stop stopInfo = data.get(position);

        // Setting all values in listview
        minutes.setText("0");
        route.setText(String.format("%s (%s)", stopInfo.getRoute(), stopInfo.getTransit()));
        stop.setText(stopInfo.getStop());

        return vi;
    }
}
