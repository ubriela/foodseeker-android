package edu.usc.imsc.trajectory;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;


public class TrajectoryStopOverlay extends ItemizedOverlay<TrajectoryStopOverlayItem> {
	
	private List<TrajectoryStopOverlayItem> trajectoryStopOverlayItems;
	private Context context;
	
	public TrajectoryStopOverlay(Drawable defaultMarker, Context con) {
		super(boundCenterBottom(defaultMarker));
		
		trajectoryStopOverlayItems = new ArrayList<TrajectoryStopOverlayItem>();
		context = con;
	}
	
	public void addOverlay(TrajectoryStop tramStop) {
		trajectoryStopOverlayItems.add(new TrajectoryStopOverlayItem(tramStop));
		populate();
	}
	
	public void clear() {
		trajectoryStopOverlayItems.clear();
		populate();
	}

	@Override
	protected TrajectoryStopOverlayItem createItem(int i) {
		return trajectoryStopOverlayItems.get(i);
	}

	@Override
	public int size() {
		return trajectoryStopOverlayItems.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		TrajectoryStopOverlayItem trajectoryStopOverlayItem = trajectoryStopOverlayItems.get(index);
		TrajectoryStopDialog tramStopDialog = new TrajectoryStopDialog(context, trajectoryStopOverlayItem.trajectoryStop);
		Log.e("TrajectoryStopOverlay", "clicked on trajectorystop "+trajectoryStopOverlayItem.trajectoryStop.toString());
		tramStopDialog.show();
		return true;
	}
}
