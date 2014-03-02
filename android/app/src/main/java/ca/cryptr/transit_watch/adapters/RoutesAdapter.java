package ca.cryptr.transit_watch.adapters;

import android.content.Context;

import net.sf.nextbus.publicxmlfeed.domain.Route;

import java.util.List;

public class RoutesAdapter extends NextbusListAdapter<Route> {

    public RoutesAdapter(Context context, List<Route> items) {
        super(context, items);
    }

    @Override
    protected String getText1(int position) {
        return getItem(position).getTitle();
    }

    @Override
    protected String getText2(int position) {
        return getItem(position).getTag();
    }

}
