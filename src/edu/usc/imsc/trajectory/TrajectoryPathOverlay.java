/**
 * 
 */
package edu.usc.imsc.trajectory;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * @author ubriela
 *
 */
public class TrajectoryPathOverlay extends Overlay {
	private List<GeoPoint> geoPoints;    
    private Paint pathPaint;
    
    public TrajectoryPathOverlay(List<GeoPoint> geoPoints, int color) {  
        this.geoPoints = geoPoints; 

        pathPaint = new Paint();
	    pathPaint.setStyle(Style.STROKE);
	    pathPaint.setStrokeWidth(2.0f);
	    pathPaint.setColor(color);
	    pathPaint.setAntiAlias(true);
    }

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {		
	    Projection projection = mapView.getProjection();
	    Path path = new Path();
	    Point point = new Point();
	    
	    for (int i = 0; i < geoPoints.size() ; i++) {
		    projection.toPixels(geoPoints.get(i), point);
		    
		    if (i == 0) {
			    path.moveTo(point.x, point.y);
		    } else if (i == geoPoints.size() - 1) {
		    	path.moveTo(point.x, point.y);
		    	Point tmp_point = new Point();
		    	projection.toPixels(geoPoints.get(geoPoints.size() - 2), tmp_point);
		    	path.lineTo(tmp_point.x, tmp_point.y);
		    	//Log.d("ccc", geoPoints.get(i).getLatitudeE6() + ","+ geoPoints.get(i).getLongitudeE6());
		    } else {
		    	path.lineTo(point.x, point.y);
		    }
	    }
	    
	    path.close(); // draw from the last point to the first point
	    
	    canvas.drawPath(path, pathPaint);
	    super.draw(canvas, mapView, shadow);
	}  
}
