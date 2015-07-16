package edu.usc.imsc.trajectory;

import com.google.android.maps.OverlayItem;


/**
 * @author infolab
 */
public class TrajectoryStopOverlayItem extends OverlayItem {

	public TrajectoryStop trajectoryStop;

	public TrajectoryStopOverlayItem(TrajectoryStop trajectoryStop) {

		super(trajectoryStop.getCoord(), trajectoryStop.getTime(), trajectoryStop.getTime());
		this.trajectoryStop = trajectoryStop;
	}
}
