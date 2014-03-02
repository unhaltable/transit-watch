package ca.cryptr.transit_watch.adapters;

import android.content.Context;

import net.sf.nextbus.publicxmlfeed.domain.Direction;

import java.util.List;

public class DirectionsAdapter extends NextbusListAdapter<Direction> {

    public DirectionsAdapter(Context context, List<Direction> items) {
        super(context, items);
    }

    @Override
    protected String getText1(int position) {
        return getItem(position).getTitle();
    }

    @Override
    protected String getText2(int position) {
        return getItem(position).getName();
    }

}
