package ca.cryptr.transit_watch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.cryptr.transit_watch.R;

public abstract class NextbusListAdapter<T> extends ArrayAdapter<T> {

    public NextbusListAdapter(Context context, List<T> items) {
        super(context, R.layout.nextbus_list_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.nextbus_list_item, null);

        // Lookup view for data population
        @SuppressWarnings("ConstantConditions")
        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);

        // Populate the data into the template view using the data object
        text1.setText(getText1(position));
        text2.setText(getText2(position));
        // Return the completed view to render on screen
        return convertView;
    }

    protected abstract String getText1(int position);

    protected abstract String getText2(int position);

}
