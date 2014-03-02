package ca.cryptr.transit_watch.weather;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CityParser extends AsyncTask<Void, Void, Void> {

    private String url, cityName, forecast, summary, tempunits, temp;
    private TextView cityText, tempText, summaryText;

    public CityParser(String url, String cityName, TextView city, TextView temp, TextView summary) {
        this.url = url;
        this.cityName = cityName;
        this.cityText = city;
        this.tempText = temp;
        this.summaryText = summary;
    }

    /**
     * Fetch the relevant weather data for the City.
     */
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            InputStream is = (new URL(url)).openStream();

            DocumentBuilder docBuilder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = docBuilder.parse(is);
            XPath xpath = XPathFactory.newInstance().newXPath();

            forecast = (String) xpath.evaluate("/siteData/forecastGroup/forecast/period/@textForecastName", xmlDoc, XPathConstants.STRING);
            summary = (String) xpath.evaluate("/siteData/forecastGroup/forecast/cloudPrecip/textSummary", xmlDoc, XPathConstants.STRING);
            tempunits = (String) xpath.evaluate("/siteData/forecastGroup/forecast/temperatures/temperature/@units", xmlDoc, XPathConstants.STRING);
            temp = (String) xpath.evaluate("/siteData/forecastGroup/forecast/temperatures/temperature", xmlDoc, XPathConstants.STRING);

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (XPathExpressionException e) {
        }

        return null;
    }

    /**
     * Display the weather data.
     */
    @Override
    protected void onPostExecute(Void voids) {
        cityText.setText(String.format("%s %s", cityName, forecast.toLowerCase()));
        tempText.setText(String.format("%s°%s", temp, tempunits));
        summaryText.setText(summary.replaceAll("\n", " "));
    }
}
