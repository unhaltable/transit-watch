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
    private TextView temp, summary;

    public SiteListParser(String cityName, TextView temp, TextView summary) {
        cities = new Cities();
        this.cityName = cityName;
        this.temp = temp;
        this.summary = summary;
    }

    public static Cities getCities() {
        return cities;
    }

    @Override
    protected Cities doInBackground(Void... voids) {
        try {
            URL url = new URL("http://dd.weather.gc.ca/citypage_weather/xml/siteList.xml");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            // Read first 2 lines, which are useless to us
            in.readLine(); // xml declaration
            in.readLine(); // siteList tag

            String str;
            int i = 1;
            City city = new City();

            while ((str = in.readLine()) != null) {
                // The end of the siteList tag (also the EOF)
                if (str.equals("</siteList>"))
                    break;

                switch (i) {
                    case 1:
                        city = new City();
                        city.setCode(str.trim().substring(12, 20));
                        break;
                    case 2:
                        city.setNameEn(str.replaceAll("\\<.*?\\>", "").trim());
                        break;
                    case 4:
                        city.setProvince(str.replaceAll("\\<.*?\\>", "").trim());
                        break;
                    case 5:
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

    @Override
    protected void onPostExecute(Cities cities) {
        summary.setText(cities.getCode(cityName));

        String url =
                String.format("http://dd.weather.gc.ca/citypage_weather/xml/%s/%s_e.xml",
                        cities.getProvince("Toronto"), cities.getCode("Toronto"));

        // Get weather info
    }
}
