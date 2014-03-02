package ca.cryptr.transit_watch.weather;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class SiteListParser extends AsyncTask<Void, Void, Cities> {

    private static Cities cities;
    private String cityName;
    private TextView city, temp, summary;

    public SiteListParser(String cityName, TextView city, TextView temp, TextView summary) {
        cities = new Cities();
        this.cityName = cityName;
        this.city = city;
        this.temp = temp;
        this.summary = summary;
    }

    /**
     * Read the siteList.xml file and store it into a HashMap.
     */
    @Override
    protected Cities doInBackground(Void... voids) {
        try {
            URL url = new URL("http://dd.weather.gc.ca/citypage_weather/xml/siteList.xml");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            // Read first 2 lines, which are useless to us
            in.readLine(); // xml declaration
            in.readLine(); // siteList tag

            // Go through the rest of the file and map the cities
            String str;
            int i = 1;
            City city = new City();

            while ((str = in.readLine()) != null) {
                // The end of the siteList tag (also the EOF)
                if (str.equals("</siteList>"))
                    break;

                // Construct and store a City
                switch (i) {
                    case 1: // code
                        city = new City();
                        city.setCode(str.trim().substring(12, 20));
                        break;
                    case 2: // nameEn
                        city.setNameEn(str.replaceAll("\\<.*?\\>", "").trim());
                        break;
                    case 4: // province
                        city.setProvince(str.replaceAll("\\<.*?\\>", "").trim());
                        break;
                    case 5: // finish constructing and store City
                        i = 0;
                        cities.addCity(city);
                        break;
                    default:
                        break;
                }

                i++;
            }

            in.close();

            return cities;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return null;
    }

    /**
     * Build a URL based on the city info, and retrieve the weather data for that city.
     */
    @Override
    protected void onPostExecute(Cities cities) {
        String[] cityInfo = cities.getCity(cityName);

        String url =
                String.format("http://dd.weather.gc.ca/citypage_weather/xml/%s/%s_e.xml",
                        cityInfo[1], cityInfo[0]);

        // Get weather info
        new CityParser(url, cityName, city, temp, summary).execute();
    }
}
