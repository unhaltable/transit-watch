package ca.cryptr.transit_watch.weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationChecker {

    private Location getLocation(Context context) {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

//        Location lastKnownLocation = null;
//
//        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
//            lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//            lastKnownLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public String getCity(Context context) throws IOException {
        Location loc = getLocation(context);

        if (loc == null)
            throw new IOException();

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

        if (addresses.size() > 0)
            return addresses.get(0).getLocality();
        else
            throw new IOException();
    }

}
