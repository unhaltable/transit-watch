package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import ca.cryptr.transit_watch.R;

public class SelectStopActivity extends Activity {

    private Agency agency;
    private Route route;
    private Direction direction;
    private Stop stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        SelectAgencyFragment transit = new SelectAgencyFragment();
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_add_cancel:
                this.finish();
                return true;
            case R.id.action_add_previous_transit:
                SelectAgencyFragment prevTransit = new SelectAgencyFragment();
                ft.replace(R.id.fragment_add, prevTransit);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_previous_route:
                SelectRouteFragment prevRoute = new SelectRouteFragment();
                ft.replace(R.id.fragment_add, prevRoute);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_previous_dir:
                SelectDirectionFragment prevDir = new SelectDirectionFragment();
                ft.replace(R.id.fragment_add, prevDir);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_done:
                // Save stop

                finish();
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
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

}
