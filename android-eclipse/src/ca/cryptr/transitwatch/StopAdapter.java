package ca.cryptr.transitwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;

	public StopAdapter(Context context, String[] values) {
		super(context, R.layout.stop_list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.stop_list_item, parent, false);

		TextView minutes = (TextView) rowView.findViewById(R.id.minutes);
		TextView route = (TextView) rowView.findViewById(R.id.route);
		TextView stop = (TextView) rowView.findViewById(R.id.stop);

		minutes.setText(values[position]);
		route.setText(values[position]);
		stop.setText(values[position]);

		return rowView;
	}

}
