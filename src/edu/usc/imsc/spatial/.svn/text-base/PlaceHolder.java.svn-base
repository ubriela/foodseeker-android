package edu.usc.imsc.spatial;

import java.util.LinkedList;
import java.util.List;

/**
 * This class holds onto a list of USC buildings.
 * @author  William Quach, Nga Chung
 */
public class PlaceHolder {
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static PlaceHolder instance;
	/**
	 * @uml.property  name="places"
	 */
	private List<Building> places;
	
	private PlaceHolder() {
		places = new LinkedList<Building>();
	}
	/**
	 * Returns the current list of Places.
	 * @return  current list of Places
	 * @uml.property  name="places"
	 */
	public List<Building> getPlaces() {
		return places;
	}

	/**
	 * Sets the current list of Places.
	 * @param places  current list of Places.
	 * @uml.property  name="places"
	 */
	public void setPlaces(List<Building> places) {
		this.places = places;
	}	
	/**
	 * Returns instance of PlaceHolder.
	 * @return  instance of PlaceHolder
	 * @uml.property  name="instance"
	 */
	public static PlaceHolder getInstance() {
		if (instance == null) {
			instance = new PlaceHolder();
		}
		return instance;
	}
}
