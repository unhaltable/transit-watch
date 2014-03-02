package ca.cryptr.transit_watch.adapters;

import android.content.Context;

import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.List;

public class StopsAdapter extends NextbusListAdapter<Stop> {

    public StopsAdapter(Context context, List<Stop> items) {
        super(context, items);
    }

    @Override
    protected String getText1(int position) {
        return getItem(position).getTitle();
    }

    @Override
    protected String getText2(int position) {
        Stop s = getItem(position);
        return String.format("ID %s: (%s, %s)", s.getTag(),
                             s.getGeolocation().getLatitude(), s.getGeolocation().getLongitude());
    }
}
