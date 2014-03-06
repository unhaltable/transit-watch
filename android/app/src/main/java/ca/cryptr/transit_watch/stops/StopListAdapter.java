package ca.cryptr.transit_watch.stops;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.Random;

import ca.cryptr.transit_watch.R;
import ca.cryptr.transit_watch.activities.StopsActivity;
import ca.cryptr.transit_watch.preferences.PreferencesDataSource;

public class StopListAdapter extends ResourceCursorAdapter {

    private LinearLayout etaView;
    private TextView minutesView;
    private TextView routeView;
    private TextView stopView;

    private Agency agency;
    private Route route;
    private Stop stop;

    private PredictionGroup predictions;
    private int eta;

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
        etaView = (LinearLayout) view.findViewById(R.id.eta);
        minutesView = (TextView) view.findViewById(R.id.minutes);
        routeView = (TextView) view.findViewById(R.id.route);
        stopView = (TextView) view.findViewById(R.id.stop);

        // Keep references to the column positions of each property
        PreferencesDataSource.AgencyCursorColumns agencyCursorColumns = PreferencesDataSource.AgencyCursorColumns.fromCursor(cursor);
        PreferencesDataSource.RouteCursorColumns routeCursorColumns = PreferencesDataSource.RouteCursorColumns.fromCursor(cursor);
        PreferencesDataSource.DirectionCursorColumns directionCursorColumns = PreferencesDataSource.DirectionCursorColumns.fromCursor(cursor);
        PreferencesDataSource.StopCursorColumns stopCursorColumns = PreferencesDataSource.StopCursorColumns.fromCursor(cursor);

        // Save the agency, routeView, direction tag for the next group of stops
        agency = PreferencesDataSource.agencyFromCursor(cursor, agencyCursorColumns);
        route = PreferencesDataSource.routeFromCursor(cursor, routeCursorColumns, agency);
        stop = PreferencesDataSource.stopFromCursor(cursor, stopCursorColumns, agency);

        // ETA
//        Random generator = new Random();
//        int i = generator.nextInt(30) + 1;

        int eta_min = 15; // get the actual eta
        minutesView.setText(String.valueOf(eta_min));
        if (eta_min > 10)
            etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_green));
        else if (eta_min > 5)
            etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_orange));
        else
            etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_red));

//        new GetSchedule().execute();

        // Route info
        routeView.setText(String.format("%s (%s)", route.getTitle(), agency.getShortTitle()));
        stopView.setText(stop.getTitle());
    }

    protected class GetSchedule extends AsyncTask<Void, Void, PredictionGroup> {
        @Override
        protected PredictionGroup doInBackground(Void... params) {
            predictions = StopsActivity.getNextbusService().getPredictions(route, stop);
            return predictions;
        }
        @Override
        protected void onPostExecute(PredictionGroup items) {
            try {
//                System.out.println(predictions.getDirections().get(0).getPredictions().get(0).getPredictedArrivalOrDepartureMinutes());
//                System.out.println(predictions.getDirections());
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("ERROR");
            }

//            minutesView.setText(String.valueOf(eta_min));
//            if (eta > 10)
//                etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_green));
//                etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_orange));
//            else
//                etaView.setBackgroundColor(context.getResources().getColor(R.color.stop_red));
        }
    }
}
