package ca.cryptr.transit_watch.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;

import ca.cryptr.transit_watch.R;

public class AddStopActivity extends Activity {

    private static String agency;
    private static String route;
    private static String stop;
    private static String agencyTag;
    private static String routeTag;
    private static Route routeObj;
    private static String dirTag;
    private static Direction dirObj;
    private static String stopTag;

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
            case R.id.action_add_previous_dir:
                AddDirectionFragment prevDir = new AddDirectionFragment();
                ft.replace(R.id.fragment_add, prevDir);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_add_done:
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

    public static String getAgency() {
        return agency;
    }

    public static void setAgency(String agency) {
        AddStopActivity.agency = agency;
    }

    public static String getRoute() {
        return route;
    }

    public static void setRoute(String route) {
        AddStopActivity.route = route;
    }

    public static String getStop() {
        return stop;
    }

    public static void setStop(String stop) {
        AddStopActivity.stop = stop;
    }

    public static String getStopTag() {
        return stopTag;
    }

    public static void setStopTag(String stopTag) {
        AddStopActivity.stopTag = stopTag;
    }

    public static String getAgencyTag() {
        return agencyTag;
    }

    public static void setAgencyTag(String agencyTag) {
        AddStopActivity.agencyTag = agencyTag;
    }

    public static String getRouteTag() {
        return routeTag;
    }

    public static void setRouteTag(String routeTag) {
        AddStopActivity.routeTag = routeTag;
    }

    public static Route getRouteObj() {
        return routeObj;
    }

    public static void setRouteObj(Route routeObj) {
        AddStopActivity.routeObj = routeObj;
    }

    public static String getDirTag() {
        return dirTag;
    }

    public static Direction getDirObj() {
        return dirObj;
    }

    public static void setDirObj(Direction dirObj) {
        AddStopActivity.dirObj = dirObj;
    }

    public static void setDirTag(String dirTag) {
        AddStopActivity.dirTag = dirTag;
    }
}
