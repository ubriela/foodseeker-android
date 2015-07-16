package edu.usc.imsc.statistic;


/**
 *
 * @author ubriela
 */
public class Statistic {
    private int numberOfSessions = 0;
    private float speed = 0;
    private String times ;

    public String getTimes() {
	return times;
    }

    public void setTimes(String times) {
	this.times = times;
    }



    public int getNumberOfSessions() {
	return numberOfSessions;
    }

    public void setNumberOfSessions(int numberOfSessions) {
	this.numberOfSessions = numberOfSessions;
    }

    public float getSpeed() {
	return speed;
    }

    public void setSpeed(float speed) {
	this.speed = speed;
    }
    
    
}
