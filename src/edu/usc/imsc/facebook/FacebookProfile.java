package edu.usc.imsc.facebook;

public class FacebookProfile {
	private String id;
	private String name;
	private String firstName="";
	private String lastName="";
	private String email="";
	
	public FacebookProfile(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	public String getEmail(){
		return email;
	}

}


//public class FacebookProfile {
//	private String id;
//	private String name;
//	private String firstName;
//	private String lastName;
//	
//	public FacebookProfile(String id, String name){
//		this.id = id;
//		this.name = name;
//	}
//	
//	public void setFirstName(String firstName){
//		this.firstName = firstName;
//	}
//	
//	public void setLastName(String lastName){
//		this.lastName = lastName;
//	}
//	
//	public String getId(){
//		return id;
//	}
//	
//	public String getName(){
//		return name;
//	}
//	
//	public String getFirstName(){
//		return firstName;
//	}
//	
//	public String getLastName(){
//		return lastName;
//	}
//
//}
