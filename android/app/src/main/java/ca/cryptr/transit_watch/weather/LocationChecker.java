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

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER

        return locationManager.getLastKnownLocation(locationProvider);
    }

    public String getCity(Context context) throws IOException {
        Location loc = getLocation(context);

        if (loc != null) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (addresses.size() > 0)
                return addresses.get(0).getLocality();
        }

        return "Error";
    }

}
