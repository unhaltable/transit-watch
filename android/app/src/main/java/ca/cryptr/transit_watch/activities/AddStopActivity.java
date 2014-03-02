package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.cryptr.transit_watch.R;

public class AddStopActivity extends Activity {

    private static String transit = "", route = "", stop = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        AddTransitFragment transit = new AddTransitFragment();
        ft.add(R.id.fragment_add, transit);
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
//        Intent intent = new Intent(this, StopsActivity.class);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_add_cancel:
                this.finish();
                return true;
            case R.id.action_add_previous_transit:
                AddTransitFragment prevTransit = new AddTransitFragment();
                ft.replace(R.id.fragment_add, prevTransit);
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
//                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // Save stop

                this.finish();
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

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }
}
