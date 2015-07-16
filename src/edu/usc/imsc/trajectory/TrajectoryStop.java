package edu.usc.imsc.trajectory;

import com.google.android.maps.GeoPoint;

/**
 * @author infolab
 */
public class TrajectoryStop {

	private int id;
	private String time;
	private GeoPoint coord;
	
	public TrajectoryStop(int id, String time, GeoPoint coord) {
		super();
		this.id = id;
		this.time = time;
		this.coord = coord;
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public GeoPoint getCoord() {
		return coord;
	}
	public void setCoord(GeoPoint coord) {
		this.coord = coord;
	}
}
