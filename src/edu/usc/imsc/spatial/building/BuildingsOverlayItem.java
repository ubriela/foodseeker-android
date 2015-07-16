package edu.usc.imsc.spatial.building;

import com.google.android.maps.OverlayItem;

import edu.usc.imsc.spatial.Place;

/**
 * This class represents a building overlay item to be displayed on Google map.
 * 
 * @original author William Quach, Nga Chung
 * @author Ling Hu
 */
public class BuildingsOverlayItem extends OverlayItem {

	private Place place;

	/**
	 * Constructs building overlay item.
	 * 
	 * @param place
	 *            place object
	 */
	public BuildingsOverlayItem(Place place) {
		super(place.getLocation(), place.getName(), place.getShortName());
		this.place = place;
	}

	/**
	 * Returns ID of item.
	 * 
	 * @return ID of item
	 * @uml.property name="id"
	 */
	public Place getPlace() {
		return this.place;
	}
}
