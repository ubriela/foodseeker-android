package edu.usc.imsc.trajectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.maps.GeoPoint;



/**
 * @author infolab
 */
public class TrajectoryPath {

	private Map<Integer, TrajectoryStop> stops;
	private List<GeoPoint> coords;
	
	public TrajectoryPath() {
		super();
		stops = new HashMap<Integer, TrajectoryStop>();
		coords = new ArrayList<GeoPoint>();
	}

	public TrajectoryPath(List<GeoPoint> coords, Map<Integer, TrajectoryStop> stops) {
		super();
		this.coords = coords;
		this.stops = stops;
	}
	
	public Map<Integer, TrajectoryStop> getStops() {
		return stops;
	}
	
	public List<GeoPoint> getCoords() {
		return coords;
	}
}
