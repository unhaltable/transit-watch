package ca.cryptr.transit_watch.adapters;

import android.content.Context;

import net.sf.nextbus.publicxmlfeed.domain.Agency;

import java.util.List;

public class AgenciesAdapter extends NextbusListAdapter<Agency> {

    public AgenciesAdapter(Context context, List<Agency> items) {
        super(context, items);
    }

    @Override
    protected String getText1(int position) {
        Agency a = getItem(position);
        return String.format("%s%s",
                             a.getTitle(),
                             a.getShortTitle() != null ? String.format(" (%s)", a.getShortTitle()) : "");
    }

    @Override
    protected String getText2(int position) {
        return getItem(position).getRegionTitle();
    }

}
