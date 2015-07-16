package edu.usc.imsc.spatial;

import java.util.LinkedList;
import java.util.List;

/**
 * This class holds onto a list of USC Place.
 * @author  William Quach, Nga Chung
 */
public class PlaceHolder2 {
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static PlaceHolder2 instance;
	/**
	 * @uml.property  name="Places"
	 */
	private List<Place> Places;
	
	private PlaceHolder2() {
		Places = new LinkedList<Place>();
	}
	/**
	 * Returns the current list of Places.
	 * @return  current list of Places
	 * @uml.property  name="Places"
	 */
	public List<Place> getPlaces() {
		return Places;
	}

	/**
	 * Sets the current list of Places.
	 * @param Places  current list of Places.
	 * @uml.property  name="Places"
	 */
	public void setPlaces(List<Place> Places) {
		this.Places = Places;
	}	
	/**
	 * Returns instance of PlaceHolder.
	 * @return  instance of PlaceHolder
	 * @uml.property  name="instance"
	 */
	public static PlaceHolder2 getInstance() {
		if (instance == null) {
			instance = new PlaceHolder2();
		}
		return instance;
	}
}
