package ca.cryptr.transit_watch.stops;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.Random;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;

public class StopListAdapter extends ResourceCursorAdapter {

    public StopListAdapter(Context context, Cursor c) {
        super(context, R.layout.stop_list_item, c, false);
    }

    @Override
    public Object getItem(int position) {
        Cursor cursor = (Cursor) super.getItem(position);

        // Keep references to the column positions of each property
        PreferencesDataSource.AgencyCursorColumns agencyCursorColumns = PreferencesDataSource.AgencyCursorColumns.fromCursor(cursor);
        PreferencesDataSource.StopCursorColumns stopCursorColumns = PreferencesDataSource.StopCursorColumns.fromCursor(cursor);

        // Save the agency, routeView, direction tag for the next group of stops
        Agency agency = PreferencesDataSource.agencyFromCursor(cursor, agencyCursorColumns);

        return PreferencesDataSource.stopFromCursor(cursor, stopCursorColumns, agency);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LinearLayout etaView = (LinearLayout) view.findViewById(R.id.eta);
        TextView minutesView = (TextView) view.findViewById(R.id.minutes);
        TextView routeView = (TextView) view.findViewById(R.id.route);
        TextView stopView = (TextView) view.findViewById(R.id.stop);

        // ETA
        Random generator = new Random();
        int i = generator.nextInt(30) + 1;

        int eta_min = i; // get the actual eta
        minutesView.setText(String.valueOf(eta_min));
        if (eta_min > 10)
            etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_green));
        else if (eta_min > 5)
            etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_orange));
        else
            etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_red));

        // Keep references to the column positions of each property
        PreferencesDataSource.AgencyCursorColumns agencyCursorColumns = PreferencesDataSource.AgencyCursorColumns.fromCursor(cursor);
        PreferencesDataSource.RouteCursorColumns routeCursorColumns = PreferencesDataSource.RouteCursorColumns.fromCursor(cursor);
        PreferencesDataSource.DirectionCursorColumns directionCursorColumns = PreferencesDataSource.DirectionCursorColumns.fromCursor(cursor);
        PreferencesDataSource.StopCursorColumns stopCursorColumns = PreferencesDataSource.StopCursorColumns.fromCursor(cursor);

        // Save the agency, routeView, direction tag for the next group of stops
        Agency agency = PreferencesDataSource.agencyFromCursor(cursor, agencyCursorColumns);
        Route route = PreferencesDataSource.routeFromCursor(cursor, routeCursorColumns, agency);
        Stop stop = PreferencesDataSource.stopFromCursor(cursor, stopCursorColumns, agency);

        // Route info
        routeView.setText(String.format("%stop (%stop)", route.getTitle(), agency.getTitle()));
        stopView.setText(stop.getTitle());
    }
}
