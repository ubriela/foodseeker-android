package edu.usc.imsc.food;

import com.google.android.maps.OverlayItem;

import edu.usc.imsc.spatial.Place;

/**
 * This class represents a building overlay item to be displayed on Google map.
 * 
 * @original author William Quach, Nga Chung
 * @author Ling Hu
 */
public class FoodOverlayItem extends OverlayItem {

	private Place place;
	private Food food;

	/**
	 * Constructs building overlay item.
	 * 
	 * @param place
	 *            place object
	 */
	public FoodOverlayItem(Place place) {
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

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
	
}
