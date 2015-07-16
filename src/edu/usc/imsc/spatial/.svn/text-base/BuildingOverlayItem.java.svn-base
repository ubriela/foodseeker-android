package edu.usc.imsc.spatial;

import com.google.android.maps.OverlayItem;


/**
 * This class represents a building overlay item to be displayed on Google map.
 * @original  author William Quach, Nga Chung
 * @author  Ling Hu
 */
public class BuildingOverlayItem extends OverlayItem {

	private Building place; 
	
	/**
	 * Constructs building overlay item.
	 * 
	 * @param place
	 *            place object
	 */
	public BuildingOverlayItem(Building place) {
		super(place.getLocation(), place.getName(), place.getShortName());
		this.place = place;
	}

	/**
	 * Returns ID of item.
	 * @return  ID of item
	 * @uml.property  name="id"
	 */
	public Building getPlace() {
		return this.place;
	}
}
