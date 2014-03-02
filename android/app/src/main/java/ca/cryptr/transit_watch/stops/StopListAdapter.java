package ca.cryptr.transit_watch.stops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import ca.cryptr.transit_watch.R;

public class StopListAdapter extends ArrayAdapter<FavStop> {

    public StopListAdapter(Context context, List<FavStop> stops) {
        super(context, R.layout.stop_list_item, stops);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null)
            vi = LayoutInflater.from(getContext()).inflate(R.layout.stop_list_item, null);

        LinearLayout eta = (LinearLayout) vi.findViewById(R.id.eta);
        TextView minutes = (TextView) vi.findViewById(R.id.minutes);
        TextView route = (TextView) vi.findViewById(R.id.route);
        TextView stop = (TextView) vi.findViewById(R.id.stop);

        FavStop stopInfo = getItem(position);

        // ETA
        Random generator = new Random();
        int i = generator.nextInt(30) + 1;

        int eta_min = i; // get the actual eta
        minutes.setText(String.valueOf(eta_min));
        if (eta_min > 10)
            eta.setBackgroundColor(getContext().getResources().getColor(R.color.stop_green));
        else if (eta_min > 5)
            eta.setBackgroundColor(getContext().getResources().getColor(R.color.stop_orange));
        else
            eta.setBackgroundColor(getContext().getResources().getColor(R.color.stop_red));

        // Route info
        route.setText(String.format("%s (%s)", stopInfo.getRoute(), stopInfo.getAgency()));
        stop.setText(stopInfo.getStop());

        return vi;
    }
}
