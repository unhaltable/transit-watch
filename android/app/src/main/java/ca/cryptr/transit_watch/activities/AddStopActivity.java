package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.cryptr.transit_watch.R;

public class AddStopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        AddTransitFragment transit = new AddTransitFragment();
        ft.add(R.id.fragment_add, transit);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_stop, menu);
        return true;
    }

    /**
     * Handles all of of the menu items' selections.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, StopsActivity.class);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.action_add_cancel:
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            case R.id.action_add_next_route:
                AddRouteFragment nextRoute = new AddRouteFragment();
                ft.replace(R.id.fragment_add, nextRoute);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_previous_transit:
                AddTransitFragment prevTransit = new AddTransitFragment();
                ft.replace(R.id.fragment_add, prevTransit);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_next_stop:
                AddStopFragment nextStop = new AddStopFragment();
                ft.replace(R.id.fragment_add, nextStop);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_previous_route:
                AddRouteFragment prevRoute = new AddRouteFragment();
                ft.replace(R.id.fragment_add, prevRoute);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_done:
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Allows for the back button to go back to the previous fragment.
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
