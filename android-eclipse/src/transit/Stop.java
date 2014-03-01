package transit;

import java.io.Serializable;

public class Stop implements Serializable {

	private static final long serialVersionUID = 3620048110145714278L;

	private String routeName;
	private String stopName;

	public Stop(String routeName, String stopName) {
		this.routeName = routeName;
		this.stopName = stopName;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

}
