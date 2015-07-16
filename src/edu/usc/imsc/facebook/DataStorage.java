package edu.usc.imsc.facebook;

import java.util.ArrayList;



public class DataStorage {
	private static FacebookProfile me = null;
	private static ArrayList friends = null;
	private static ArrayList posts = null;
	private static boolean loggedIn = false;
	
	
	public static void setLoggedIn(boolean loggedIn) {
		DataStorage.loggedIn = loggedIn;
	}


	public static boolean isLoggedIn(){
		return loggedIn;
	}
	
	public static void setFriends(ArrayList friends1){
	     friends = friends1;
	}
	public static ArrayList getFriends(){
		return friends;
	}
	
	public static void setMe(FacebookProfile me1){
		me = me1;
	}
	
	public static FacebookProfile getMe(){
		return me;
	}
	
	public static void setPosts(ArrayList posts1){
		posts = posts1;
	}
	public static ArrayList getPosts(){
		return posts;
	}

	public static void PostToWall(String message){
		PostEvent.firePostEvent(message);
	}
}
