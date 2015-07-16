package edu.usc.imsc.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class Tools {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static String TAG = "CSCI587";
	public static String TEST_SERVER_PORT = "10.0.2.2:8084";
	public static String SERVER_PORT = "icampus.usc.edu";
	public static final String EVENT_URL = "http://icampus.usc.edu/icampus/services/GetEvents";
	public static final String RECTANGLE_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/RectangleQueryFoodAction.do?";
	public static final String CIRCLE_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/WithinDistanceQueryFoodAction.do?";
	public static final String POLYGON_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/PolygonQueryFoodAction.do?";
	public static final String KNN_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/KNNQueryFoodAction.do?";

	//	Explore food
	public static final String TOP_FOOD_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/TopQueryFoodAction.do?";
	public static final String NEARBY_FOOD_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/NearbyQueryFoodAction.do?";
	public static final String RECENT_FOOD_SPATIAL_QUERY_SERVICE = "http://"
			+ SERVER_PORT + "/CSCI587SERVER/RecentQueryFoodAction.do?";

	//	Trajectory save location
	public static final String USER_STATISTIC_URL = "http://" + SERVER_PORT
			+ "/CSCI587SERVER/SaveLocationAction.do?";
	public static final String USER_STATISTIC_URL2 = "http://" + SERVER_PORT
			+ "/CSCI587SERVER/StatisticQueryAction.do?";

	//	Trajectory
	public static final String TRAJECDTORY_QUERY_SERVICE = "http://"
			+ TEST_SERVER_PORT + "/CSCI587SERVER/TrajectoryAction.do?";
	public static final String FOOD_LINE_QUERY_SERVICE = "http://"
			+ TEST_SERVER_PORT + "/CSCI587SERVER/FoodLineStringQueryAction.do?";
	public static final String BUILDING_LINE_QUERY_SERVICE = "http://"
			+ TEST_SERVER_PORT
			+ "/CSCI587SERVER/BuildingLineStringQueryAction.do?";

	public static final String PREFERENCE_FILE = "MyPreferences";
	// public static final double kMinLat = 33.758599;
	// public static final double kMinLong = -118.413391;
	// public static final double kMaxLat = 34.148181;
	// public static final double kMaxLong = -118.152466;
	// public static final double USCLat = 34.020617;
	// public static final double USCLong = -118.28521;

	public static final int USC_POINT_LON = (int) (34.0214587868681 * 1E6);
	public static final int USC_POINT_LAT = (int) (-118.28657890219 * 1E6);
	public static final int MAP_ZOOM_TO_SPAN_LON = (int) 0.002E6;
	public static final int MAP_ZOOM_TO_SPAN_LAT = (int) 0.002E6;
	public static final int TRAJECTORY_POINTS_NUMBER = 9;
	public static final int TRAJECTORY_DISTANCE_BETWEEN_POINTS = 200;
	public static final int TRAJECTORY_DISTANCE_TO_LINES = 5000;
	public static final int TRAJECTORY_POINTS_SEARCH = 0; // 0 means search all

	public final static String MAP_LAYER_PARAM = "layers";
	public static final String MAP_LAYER_VALUE = "values";
	public static final int LAYER_NUMBER = 3;
	public static final int EVENT_LIST_LENGTH = 20;
	public static final String USER_PROFILE_URL = "http://" + SERVER_PORT
			+ "/icampus/SetUserInfo";

	public static final CharSequence[] queryTypes = { "Rectangle", "Polygon",
			"Circle", "KNN" };
	public static final int ZOOM_LEVEL_THRESHOLD = 5;

	public static enum QueryType {
		Rectangle, Polygon, Circle, KNN
	};

	public static String getCurrentTime() {
		return ("[" + dateFormat.format(new Date(System.currentTimeMillis())) + "] ");
	}

	public static String getTime(long millis) {
		return ("[" + dateFormat.format(new Date(millis)) + "] ");
	}

	// http://localhost:8084/CSCI587SERVER/RectangleQueryAction.do?lonLeftBelow=-118.285366&latLeftBelow=34.021393&lonRightAbove=-118.287286&latRightAbove=34.019802
	public static String getRectangleSpatialQueryService(
			Vector<Double> tappedPoints) {
		return RECTANGLE_SPATIAL_QUERY_SERVICE + "latLow="
				+ tappedPoints.get(0) + "&" + "lonLow=" + tappedPoints.get(1)
				+ "&" + "latHigh=" + tappedPoints.get(2) + "&" + "lonHigh="
				+ tappedPoints.get(3);
	}

	//
	public static String getPolygonSpatialQueryService(List<GeoPoint> geoPoints) {
		String strPoints = "";
		Iterator<GeoPoint> it = geoPoints.iterator();
		while (it.hasNext()) {
			GeoPoint point = (GeoPoint) it.next();
			strPoints += point.getLatitudeE6() / 1E6;
			strPoints += ",";
			strPoints += point.getLongitudeE6() / 1E6;
			strPoints += ",";
		}

		return POLYGON_SPATIAL_QUERY_SERVICE + "cords=" + strPoints;
	}

	public static String getCircleSpatialQueryService(List<GeoPoint> geoPoints) {
		double dis = CalculationByDistance(geoPoints.get(0), geoPoints.get(1));
		Log.d(Tools.TAG, "Tools: distance: " + String.valueOf(dis));
		return CIRCLE_SPATIAL_QUERY_SERVICE + "lat="
				+ geoPoints.get(0).getLatitudeE6() / 1E6 + "&lon="
				+ geoPoints.get(0).getLongitudeE6() / 1E6 + "&dis="
				+ String.valueOf(dis);
	}

	public static String getKNNSpatialQueryService(Vector<Double> tappedPoints) {
		return KNN_SPATIAL_QUERY_SERVICE + "lat=" + tappedPoints.get(0)
				+ "&lon=" + tappedPoints.get(1) + "&knn=" + "5";
	}

	private static double Radius = 6366000;

	public static double CalculationByDistance(GeoPoint StartP, GeoPoint EndP) {
		double lat1 = StartP.getLatitudeE6() / 1E6;
		double lat2 = EndP.getLatitudeE6() / 1E6;
		double lon1 = StartP.getLongitudeE6() / 1E6;
		double lon2 = EndP.getLongitudeE6() / 1E6;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +

		Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Radius * c;
	}

	public static String getStatisticURL(String uid) {
		// TODO Auto-generated method stub
		return USER_STATISTIC_URL2 + "uid=" + uid;
	}

	public static String getSaveLocation(String uid, GeoPoint gPoint) {
		return USER_STATISTIC_URL + "uid=" + String.valueOf(uid) + "&lat="
				+ gPoint.getLatitudeE6() / 1E6 + "&lon="
				+ gPoint.getLongitudeE6() / 1E6;
	}

	public static String getUserProfileUrlSet(String fname, String lname,
			String email, String fb_id) {
		return USER_PROFILE_URL + "?fname=" + fname + "&lname=" + lname
				+ "&email=" + email + "&fb_id=" + fb_id;
	}

	public static void callService(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);

		try {
			HttpResponse getResponse = client.execute(postRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.d("Error for URL " + statusCode, url);
			}

		} catch (IOException e) {
			postRequest.abort();
			Log.d("Error for URL " + url, e.toString());
		}
	}

	public static InputStream retrieveStream(String url) {
		org.apache.http.impl.client.DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		Log.d(Tools.TAG, url);
		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("Tools: ", "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {
			getRequest.abort();
			Log.w("Tools: ", "Error for URL " + url, e);
		}

		return null;
	}

	public static String getSaveLocation(String uid, GeoPoint gPoint,
			boolean newsession) {
		return USER_STATISTIC_URL + "uid=" + String.valueOf(uid) + "&lat="
				+ gPoint.getLatitudeE6() / 1E6 + "&lon="
				+ gPoint.getLongitudeE6() / 1E6 + "&newsession=" + newsession;
	}

	public static String getTopFoodsURL(int num) {
		// TODO Auto-generated method stub
		return TOP_FOOD_SPATIAL_QUERY_SERVICE + "num=" + num;
	}

	public static String getNearbyFoodsURL(GeoPoint gPoint, int num) {
		// TODO Auto-generated method stub
		return NEARBY_FOOD_SPATIAL_QUERY_SERVICE + "lat="
				+ gPoint.getLatitudeE6() / 1E6 + "&lon="
				+ gPoint.getLongitudeE6() / 1E6 + "&num=" + num;
	}

	public static String getRecentFoodsURL(int num) {
		// TODO Auto-generated method stub
		return RECENT_FOOD_SPATIAL_QUERY_SERVICE + "num=" + num;
	}

	public static String getTrajectoryURL(String uid, int num, int mindis) {
		// TODO Auto-generated method stub
		return TRAJECDTORY_QUERY_SERVICE + "uid=" + uid + "&num=" + num
				+ "&mindis=" + mindis;

	}

	public static String getFoodLineURL(String cords, int dis) {
		// TODO Auto-generated method stub
		return FOOD_LINE_QUERY_SERVICE + "cords=" + cords + "&dis=" + dis;
	}

	public static String getBuildingLineURL(String table, String cords, int dis) {
		// TODO Auto-generated method stub
		return BUILDING_LINE_QUERY_SERVICE + "table=" + table + "&cords="
				+ cords + "&dis=" + dis;
	}
}