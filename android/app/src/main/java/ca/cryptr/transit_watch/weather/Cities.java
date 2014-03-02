package ca.cryptr.transit_watch.weather;

import java.util.HashMap;

public class Cities {

    private HashMap<String, City> cities;

    public Cities() {
        cities = new HashMap<String, City>();
    }

    public void addCity(City city) {
        cities.put(city.getNameEn(), city);
    }

    /**
     * Returns a String array containing the City with name nameEn's code and
     * province.
     * @param nameEn The English name of the City.
     * @return The City's code and province in a String array.
     */
    public String[] getCity(String nameEn) {
        City city = cities.get(nameEn);
        return new String[] { city.getCode(), city.getProvince() };
    }

    public String getCode(String nameEn) {
        return cities.get(nameEn) != null ? cities.get(nameEn).getCode() : "    ERROR: City not in database";
    }

    public String getProvince(String nameEn) {
        return cities.get(nameEn) != null ? cities.get(nameEn).getProvince() : "    ERROR: City not in database";
    }

}
