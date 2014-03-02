package ca.cryptr.transit_watch.activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cryptr.transit_watch.R;

public class AboutFragment extends Fragment {

    private ListView devsList;
    private SimpleAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);

        setupTransitList();

        return view;
    }

    public void setupTransitList() {
        devsList = (ListView) view.findViewById(R.id.devs_list);

        final String[] devs = getResources().getStringArray(R.array.devs);

        devsList.setClickable(true);
        devsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                long position = parent.getItemIdAtPosition(pos);

                if (position == 0 || position == 1 || position == 2) {
                    String url = "";

                    if (position == 0)
                        url =  "https://twitter.com/spe_";
                    else if (position == 1)
                        url =  "https://twitter.com/arkon";
                    else if (position == 2)
                        url =  "https://twitter.com/NickGoh_";
                    else if (position == 3)
                        url =  "https://github.com/hxhl95";

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (int i = 0; i < devs.length; i+=2) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("1", devs[i]);
            item.put("2", devs[i+1]);
            data.add(item);
        }

        adapter = new SimpleAdapter(getActivity(), data,
                R.layout.nextbus_list_item,
                new String[] {"1", "2"},
                new int[] {android.R.id.text1, android.R.id.text2});

        devsList.setAdapter(adapter);
    }
}
