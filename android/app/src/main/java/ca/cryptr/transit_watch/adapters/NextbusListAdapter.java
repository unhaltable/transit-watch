package ca.cryptr.transit_watch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.ArrayList;
import java.util.List;

import ca.cryptr.transit_watch.R;

public abstract class NextbusListAdapter<T> extends BaseAdapter implements Filterable {

    private Context context;
    private int resource;

    private Filter mFilter;

    private List<T> mObjects;
    private List<T> itemsCopy;

    private LayoutInflater inflater;

    private final Object mLock = new Object();

    public NextbusListAdapter(Context context, List<T> items) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = R.layout.nextbus_list_item;
        this.mObjects = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = inflater.inflate(resource, parent, false);

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

    public T getItem(int position) {
        return mObjects.get(position);
    }

    public int getCount() {
        return mObjects.size();
    }

    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new ArrayFilter();
        return mFilter;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (itemsCopy == null) {
                synchronized (mLock) {
                    itemsCopy = new ArrayList<T>(mObjects);
                }
            }

            if (constraint == null || constraint.length() == 0) {
                ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<T>(itemsCopy);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String constraintText = constraint.toString().toLowerCase();

                ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<T>(itemsCopy);
                }

                final ArrayList<T> newValues = new ArrayList<T>();

                for (T value : values) {
                    // Get the list item contents
                    String top = "", bottom = "";

                    if (value instanceof Agency) {
                        top = String.format("%s%s",
                                ((Agency) value).getTitle(),
                                (((Agency) value).getShortTitle() != null ?
                                        String.format(" (%s)", ((Agency) value).getShortTitle()) : ""));
                        bottom = ((Agency) value).getRegionTitle();
                    } else if (value instanceof Route) {
                        top = ((Route) value).getTitle();
                        bottom = ((Route) value).getTag();
                    } else if (value instanceof Direction) {
                        top = ((Direction) value).getTitle();
                        bottom = ((Direction) value).getName();
                    } else if (value instanceof Stop) {
                        top = ((Stop) value).getTitle();
                        bottom = String.format("ID %s: (%s, %s)", ((Stop) value).getTag(),
                                ((Stop) value).getGeolocation().getLatitude(),
                                ((Stop) value).getGeolocation().getLongitude());
                    }

                    top = top.toLowerCase();
                    bottom = bottom.toLowerCase();

                    if (top.contains(constraintText) || bottom.contains(constraintText))
                        newValues.add(value);
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                //noinspection unchecked
                mObjects = (List<T>) results.values;
                notifyDataSetChanged();
            } else {
                mObjects = itemsCopy;
                notifyDataSetInvalidated();
            }
        }
    }
}
