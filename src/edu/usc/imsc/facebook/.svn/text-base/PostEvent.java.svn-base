package edu.usc.imsc.facebook;

import java.util.ArrayList;
import java.util.Iterator;

public class PostEvent {
	private static ArrayList listeners = new ArrayList();
	
	public static void addPostListener(PostListener postListener){
		listeners.add(postListener);
	}
	public static void removePostListener(PostListener postListener){
		listeners.remove(postListener);
	}
	public static void firePostEvent(String message){
		Iterator i = listeners.iterator();
		while(i.hasNext()){
			PostListener l = (PostListener)i.next();
			l.doPost(message);
		}
	}

}
