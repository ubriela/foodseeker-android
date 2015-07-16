package edu.usc.imsc.user;

/**
 * This class represents a User.
 * @original  author William Quach, Nga Chung
 * @author  linghu
 */
public class User {
	/**
	 * @uml.property  name="userId"
	 */
	private int userId;
	/**
	 * @uml.property  name="email"
	 */
	private String email;
	/**
	 * @uml.property  name="facebookEmail"
	 */
	private String facebookEmail;


	/**
	 * Constructs User object.
	 */
	public User() {

	}

	/**
	 * Constructs User object with specified email.
	 * 
	 * @param email
	 *            user's email
	 */
	public User(String email) {
		this.email = email;
	}

	/**
	 * Constructs User object with specified ID and email.
	 * 
	 * @param userId
	 *            user's ID
	 * @param email
	 *            user's email
	 */
	public User(int userId, String email) {
		this(email);
		this.userId = userId;
	}

	/**
	 * Returns user's ID.
	 * @return  user's ID
	 * @uml.property  name="userId"
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets user's ID.
	 * @param userId  user's ID
	 * @uml.property  name="userId"
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Returns user's email.
	 * @return  user's email
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets user's email.
	 * @param email  user's email
	 * @uml.property  name="email"
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * return user's facebook account email
	 * @return  user's facebook account email
	 * @uml.property  name="facebookEmail"
	 */
	public String getFacebookEmail() {
		return facebookEmail;
	}
	/**
	 * set user's facebook account email
	 * @param facebookEmail  user's facebook account email
	 * @uml.property  name="facebookEmail"
	 */
	public void setFacebookEmail(String facebookEmail) {
		this.facebookEmail = facebookEmail;
	}

	/**
	 * Returns XML of User object.
	 * 
	 * @return XML of User object
	 */
	public String toXML() {
		String xml = "<user>";
		xml += "<userId>" + this.userId + "</userId>";
		xml += "<email>" + this.email + "</email>";
		xml += "<facebookemail>" + this.facebookEmail + "</facebookemail>";
		xml += "</user>";
		return xml;
	}
}
