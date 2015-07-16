/**
 * 
 */
package edu.usc.imsc.graphic;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import edu.usc.imsc.util.Tools;
import edu.usc.imsc.util.Tools.QueryType;

public class ShapeOverlay extends Overlay {
	private List<GeoPoint> geoPoints;
	private Paint pathPaint;
	private QueryType shapeType;

	/**
	 * shapeType = 1: rectangle shapeType = 2: circle shapeType = 3: polygon
	 * 
	 * @param geoPoints
	 * @param color
	 * @param i
	 */
	public ShapeOverlay(List<GeoPoint> geoPoints, int color, QueryType shapeType) {
		this.geoPoints = new ArrayList<GeoPoint>(geoPoints);

		pathPaint = new Paint();
		pathPaint.setStyle(Style.STROKE);
		pathPaint.setColor(color);
		pathPaint.setAntiAlias(true);
		this.shapeType = shapeType;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();
		Path path = new Path();
		Point point1 = new Point();
		Point point2 = new Point();

		switch (shapeType) {
		case Rectangle:
			projection.toPixels(geoPoints.get(0), point1);
			projection.toPixels(geoPoints.get(1), point2);
			RectF rec = new RectF(point1.x, point2.y, point2.x, point1.y);
			path.addRect(rec, Path.Direction.CW);

			path.close(); // draw from the last point to the first point
			canvas.drawPath(path, pathPaint);
			super.draw(canvas, mapView, shadow);
			break;
		case Polygon:
			if (geoPoints.size() >= 2) {
				projection.toPixels(geoPoints.get(geoPoints.size() - 2), point1);
				projection.toPixels(geoPoints.get(geoPoints.size() - 1), point2);
				path.moveTo(point1.x, point1.y);
				path.lineTo(point2.x, point2.y);
			} 
			if (geoPoints.size() == 3) {
				projection.toPixels(geoPoints.get(geoPoints.size() - 1), point1);
				projection.toPixels(geoPoints.get(0), point2);
				path.moveTo(point1.x, point1.y);
				path.lineTo(point2.x, point2.y);
			}
			path.close(); // draw from the last point to the first point
			canvas.drawPath(path, pathPaint);
			super.draw(canvas, mapView, shadow);
			break;
		case Circle:
			projection.toPixels(geoPoints.get(0), point1);
			projection.toPixels(geoPoints.get(1), point2);
			double radius = Tools.CalculationByDistance(geoPoints.get(0), geoPoints.get(1));
			Log.d(Tools.TAG, "ShapeOverlay: radius " + String.valueOf(radius));
			path.addCircle(point1.x, point1.y,(float) radius, Path.Direction.CW);

			path.close(); // draw from the last point to the first point
			canvas.drawPath(path, pathPaint);
			super.draw(canvas, mapView, shadow);
			break;
	case KNN:
		break;

	}

}
}
