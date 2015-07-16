package edu.usc.imsc.SAX.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.usc.imsc.spatial.Building;

/**
 * This class handles XML parsing for places (USC buidlings).
 * 
 * @original author William Quach, Nga Chung
 * @author linghu
 */

public class PlaceHandler extends AbstractSAXHandler {
	private static final String NAME_TAG = "name";
	private static final String SHORT_NAME_TAG = "shortName";
	private static final String BUILD_ID_TAG = "id";
	private static final String BUILD_ADDRESS = "address";
	private static final String BUILD_DESCRIPTION = "description";
	private static final String BUILD_DEPARTMENT = "department";
	private static final String BUILD_DEPARTMENT_NAME = "name";
	private static final String BUILD_DEPARTMENT_WEBSITE = "website";
	private static final String BUILD_DEPARTMENT_LOCATION = "location";
	private static final String BUILD_WEBSITE = "website";

	private boolean isNameTag;
	private boolean isShortNameTag;
	private boolean isIdTag;
	private boolean isAddress;
	private boolean isDescription;
	private boolean isDepartment;
	private boolean isDepartmentName;
	private boolean isDepartmentWebsite;
	private boolean isDepartmentLocation;
	private boolean isWebsite;

	/**
	 * Constructor
	 */
	public PlaceHandler() {
		super();
		this.isNameTag = false;
		this.isShortNameTag = false;
		this.isIdTag = false;
		this.isAddress = false;
		this.isDescription = false;
		this.isDepartment = false;
		this.isDepartmentName = false;
		this.isDepartmentWebsite = false;
		this.isDepartmentLocation = false;
		this.isWebsite = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if (this.isPlacemark && isIdTag) {
			this.currentPlacemark.setId(Integer.parseInt(new String(ch, start,
					length)));
		} else if (this.isPlacemark && isNameTag) {
			this.currentPlacemark.setName(new String(ch, start, length));
		} else if (this.isPlacemark && isShortNameTag) {
			this.currentPlacemark.setShortName(new String(ch, start, length));
		} else if (this.isPlacemark && isLatTag) {
			this.currentPlacemark.setLatitude(Double.parseDouble(new String(ch,
					start, length)));
		} else if (this.isPlacemark && isLonTag) {
			this.currentPlacemark.setLongitude(Double.parseDouble(new String(
					ch, start, length)));
		} else if (this.isPlacemark && isAddress) {
			this.currentPlacemark.setAddress(new String(ch, start, length));
		} else if (this.isPlacemark && isDescription) {
			this.currentPlacemark.setDescription(new String(ch, start, length));
		} else if (this.isPlacemark && this.isDepartment) {
			if (this.isDepartmentName) {
				this.currentPlacemark.setDepartmentName(new String(ch, start, length));
			} else if (this.isDepartmentWebsite) {
				this.currentPlacemark.setDepartmentWebsite(new String(ch, start, length));
			} else if (this.isDepartmentLocation) {
				this.currentPlacemark.setDepartmentLocation(new String(ch, start, length));
			}
		} else if (this.isPlacemark && this.isWebsite) {
			this.currentPlacemark.setWebsite(new String(ch, start, length));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals(PLACEMARK_TAG)) {
			this.isPlacemark = true;
			this.currentPlacemark = new Building();
		} else if (this.isPlacemark && !isDepartment && qName.equals(NAME_TAG)) {
			this.isNameTag = true;
		} else if (this.isPlacemark && qName.equals(SHORT_NAME_TAG)) {
			this.isShortNameTag = true;
		} else if (this.isPlacemark && qName.equals(BUILD_ID_TAG)) {
			this.isIdTag = true;
		} else if (this.isPlacemark && qName.equals(LATITUDE_TAG)) {
			this.isLatTag = true;
		} else if (this.isPlacemark && qName.equals(LONGITUDE_TAG)) {
			this.isLonTag = true;
		} else if (this.isPlacemark && qName.equals(BUILD_ADDRESS)) {
			this.isAddress = true;
		} else if (this.isPlacemark && qName.equals(BUILD_DESCRIPTION)) {
			this.isDescription = true;
		} else if (this.isPlacemark && qName.equals(BUILD_DEPARTMENT)) {
			this.isDepartment = true;
		} else if (this.isPlacemark && isDepartment && qName.equals(BUILD_DEPARTMENT_NAME)) {
			this.isDepartmentName = true;
		} else if (this.isPlacemark && isDepartment && qName.equals(BUILD_DEPARTMENT_WEBSITE)) {
			this.isDepartmentWebsite = true;
		} else if (this.isPlacemark && isDepartment && qName.equals(BUILD_DEPARTMENT_LOCATION)) {
			this.isDepartmentLocation = true;
		} else if (this.isPlacemark && !isDepartment && qName.equals(BUILD_WEBSITE)) {
			this.isWebsite = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals(PLACEMARK_TAG)) {
			this.places.add(this.currentPlacemark);
			this.currentPlacemark = null;
			this.isPlacemark = false;
		} else if (this.isPlacemark && !isDepartment && qName.equals(NAME_TAG)) {
			this.isNameTag = false;
		} else if (this.isPlacemark && qName.equals(SHORT_NAME_TAG)) {
			this.isShortNameTag = false;
		} else if (this.isPlacemark && qName.equals(BUILD_ID_TAG)) {
			this.isIdTag = false;
		} else if (this.isPlacemark && qName.equals(LATITUDE_TAG)) {
			this.isLatTag = false;
		} else if (this.isPlacemark && qName.equals(LONGITUDE_TAG)) {
			this.isLonTag = false;
		} else if (this.isPlacemark && qName.equals(BUILD_ADDRESS)) {
			this.isAddress = false;
		} else if (this.isPlacemark && qName.equals(BUILD_DESCRIPTION)) {
			this.isDescription = false;
		} else if (this.isPlacemark && qName.equals(BUILD_DEPARTMENT)) {
			this.isDepartment = false;
		} else if (this.isPlacemark && isDepartment && qName.equals(BUILD_DEPARTMENT_NAME)) {
			this.isDepartmentName = false;
		} else if (this.isPlacemark && isDepartment && qName.equals(BUILD_DEPARTMENT_WEBSITE)) {
			this.isDepartmentWebsite = false;
		} else if (this.isPlacemark && isDepartment && qName.equals(BUILD_DEPARTMENT_LOCATION)) {
			this.isDepartmentLocation = false;
		} else if (this.isPlacemark && !isDepartment && qName.equals(BUILD_WEBSITE)) {
			this.isWebsite = false;
		}
	}
}
