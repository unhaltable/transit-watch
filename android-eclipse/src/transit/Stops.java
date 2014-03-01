package transit;

import java.util.Map;
import java.util.TreeMap;

public class Stops {

	private Map<String, Stop> stops;

	private static final Stops INSTANCE = new Stops();

	public Map<String, Stop> getStops() {
		return stops;
	}

	public static Stops getInstance() {
		return INSTANCE;
	}

	private Stops() {
		this.stops = new TreeMap<String, Stop>();
	}

	public void setStops(Map<String, Stop> stops) {
		this.stops = stops;
	}

	public void addStop(Stop stop) throws StopExistsException {
		if (stops.get(stop.getStopName()) == null)
			stops.put(stop.getStopName(), stop);
		else
			throw new StopExistsException();
	}

	public void addStop(String stopName) {
		stops.remove(stopName);
	}

}
