package edu.usc.imsc.SAX.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ext.DefaultHandler2;

import edu.usc.imsc.spatial.Building;

/**
 * @author linghu
 */
public class AbstractSAXHandler extends DefaultHandler2 {
	protected static final String PLACEMARK_TAG = "Placemark";
	protected static final String LATITUDE_TAG = "lat";
	protected static final String LONGITUDE_TAG = "lon";
	/**
	 * @uml.property name="places"
	 */
	protected List<Building> places;
	/**
	 * @uml.property name="currentPlacemark"
	 * @uml.associationEnd
	 */
	protected Building currentPlacemark;
	protected boolean isPlacemark;
	protected boolean isLatTag;
	protected boolean isLonTag;

	public AbstractSAXHandler() {
		this.places = new ArrayList<Building>();
		this.isLonTag = false;
		this.isLatTag = false;
		this.isPlacemark = false;
	}

	/**
	 * Returns list of Place objects that have been parsed from XML.
	 * 
	 * @return list of Place objects that have been parsed from XML
	 * @uml.property name="places"
	 */
	public List<Building> getPlaces() {
		return this.places;
	}

}
