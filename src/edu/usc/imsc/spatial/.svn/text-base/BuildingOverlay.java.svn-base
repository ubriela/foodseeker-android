package edu.usc.imsc.spatial;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

import edu.usc.imsc.listener.BuildingOverlayListener;

/**
 * This class represents an overlay used to display USC buildings as pins on Google Map. Allows user's to select pins.
 * @original  author William Quach, Nga Chung
 * @author  linghu
 */
public class BuildingOverlay extends ItemizedOverlay<BuildingOverlayItem> {

	private List<BuildingOverlayItem> overlays;

	private BuildingOverlayItem chosenItem;
	private MapView map;
	private List<BuildingOverlayListener> listeners;
	private Drawable marker;
	private Context context;

	/**
	 * Contructs overlay
	 * 
	 * @param defaultMarker
	 *            marker to display on overlay
	 * @param map
	 *            MapView
	 */
	public BuildingOverlay(Drawable defaultMarker, MapView map, Context con) {
		super(defaultMarker);
		overlays = new ArrayList<BuildingOverlayItem>();
		listeners = new LinkedList<BuildingOverlayListener>();
		this.map = map;
		this.marker = defaultMarker;
		context = con;
	}

	/**
	 * Adds BuildingOverlayListener to overlay's listener list
	 * 
	 * @param listener
	 *            BuildingOverlayListener
	 */
	public void addBuildingOverlayListener(BuildingOverlayListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes BuildingOverlayListener from overlay's listener list
	 * 
	 * @param listener
	 *            BuildingOverlayListener
	 */
	public void removeBuildingOverlayListener(BuildingOverlayListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Returns selected building's name.
	 * 
	 * @return selected building's name
	 */
	public String getSelectedPlaceName() {
		return chosenItem.getTitle();
	}

	/**
	 * Returns selected building's ID.
	 * 
	 * @return selected building's ID
	 */
	public int getSelectedPlaceId() {
		return chosenItem.getPlace().getId();
	}


	@Override
	protected boolean onTap(int index) {
//		chosenItem = overlays.get(index);
//		if (chosenItem != null) {
//			notifyListeners();
//		}
//		map.invalidate();
//		return true;
		
		BuildingOverlayItem buildingOverlayItem = overlays.get(index);
		BuildingDialog buildingDialog = new BuildingDialog(context, buildingOverlayItem.getPlace());
		Log.e("BuildingOverlay", "clicked on building " + buildingOverlayItem.getTitle());
		buildingDialog.show();
		return true;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		boundCenterBottom(this.marker);
		drawInfoBox(canvas, mapView);
	}

	/**
	 * Adds BuildingOverlayItem to this overlay.
	 * 
	 * @param overlay
	 *            BuildingOverlayItem
	 */
	public void addOverlay(BuildingOverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}

	private void drawInfoBox(Canvas canvas, MapView mapView) {
		if (chosenItem != null) {
			Point screenLocation = mapView.getProjection().toPixels(
					chosenItem.getPoint(), null);

			int size = chosenItem.getSnippet().length() * 3 + 10;
			RectF infoBox = new RectF(screenLocation.x - size,
					screenLocation.y - 12, screenLocation.x + size,
					screenLocation.y + 12);

			Paint paint = new Paint();
			paint.setAlpha(0);
			paint.setColor(Color.GRAY);

			Paint borderPaint = new Paint();
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);

			canvas.drawRoundRect(infoBox, 5, 5, paint);
			canvas.drawRoundRect(infoBox, 5, 5, borderPaint);

			Paint fontColor = new Paint();
			fontColor.setColor(Color.WHITE);

			canvas.drawText(chosenItem.getSnippet(), screenLocation.x - size
					+ 8, screenLocation.y + 4, fontColor);
		}
	}


	@Override
	protected BuildingOverlayItem createItem(int i) {
		return overlays.get(i);
	}


	@Override
	public int size() {
		return overlays.size();
	}

	private void notifyListeners() {
		for (BuildingOverlayListener l : listeners) {
			l.onPlaceSelectionChange();
		}
	}

	/**
	 * Sets the currently selected overlay item as the specified item.
	 * 
	 * @param chosenItem
	 *            specified item to select
	 */
	public void setSelectedItem(BuildingOverlayItem chosenItem) {
		Log.i("ITEM CHOSEN", chosenItem.getPlace().getId() + "");
		this.chosenItem = chosenItem;
		map.invalidate();
	}
	
	public void setSelectedItem(int id) {
		
		for (BuildingOverlayItem item : overlays) {
			if (item.getPlace().getId() == id) {
				this.chosenItem = item;
				break;
			}
		}
		map.invalidate();
	}
} 
