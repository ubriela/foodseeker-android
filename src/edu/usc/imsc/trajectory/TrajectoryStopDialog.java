package edu.usc.imsc.trajectory;

import java.util.List;
import java.util.Set;

import edu.usc.imsc.R;
import edu.usc.imsc.activities.MapBrowserActivity;
import edu.usc.imsc.food.Food;
import edu.usc.imsc.food.FoodJsonParser;
import edu.usc.imsc.food.FoodOverlay;
import edu.usc.imsc.food.FoodOverlayItem;
import edu.usc.imsc.spatial.building.BuildingsJsonParser;
import edu.usc.imsc.spatial.Place;
import edu.usc.imsc.spatial.building.Buildings;
import edu.usc.imsc.util.Tools;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author infolab
 */
public class TrajectoryStopDialog extends Dialog {

	static final int FB_BLUE = 0xFF6D84B4;
	static final LayoutParams DEFAULT_LANDSCAPE = new LayoutParams(460, 260);
	static final LayoutParams DEFAULT_PORTRAIT = new LayoutParams(280, 420);
	static final LayoutParams FILL = new LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 6;
	static final int PADDING = 2;
	static final String FB_ICON = "icon.png";

	private String eventHTML;
	private LinearLayout mContent;
	private TextView mTitle;
	private WebView mWebView;
	private Context context;

	private TrajectoryStop trajectoryStop;

	public TrajectoryStopDialog(Context context, TrajectoryStop trajectoryStop) {
		super(context);
		this.context = context;
		this.trajectoryStop = trajectoryStop;
		eventHTML = generateEventHTML(trajectoryStop);
	}

	private String generateEventHTML(TrajectoryStop trajectoryStop) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p><b>Time: </b>" + trajectoryStop.getTime() + "</p>");
		sb.append("<p><b>Position: </b>"
				+ trajectoryStop.getCoord().getLatitudeE6() / 1e6 + ", "
				+ trajectoryStop.getCoord().getLongitudeE6() / 1e6 + "</p>");

		// "34.019967,-118.29283,34.022902,-118.288222,34.020209,-118.285686";
		String query = "";
		String points = MapBrowserActivity.trajectoryPath.getCoords()
		.toString();
		
		if (Tools.TRAJECTORY_POINTS_SEARCH == 0) {
			query = points.replaceAll(" ", "");
			Log.d("abcd", query);
			query = query.substring(1, query.length() - 1);
			String[] arr = query.split(",");
			String temp = "";
			for (int i = 0; i < arr.length; i++) {
				temp += Integer.valueOf(arr[i]) / 1e6 + ",";
			}
			query = temp.substring(0, temp.length() - 1);
		} else {
			String point = trajectoryStop.getCoord().getLatitudeE6() + ","
					+ trajectoryStop.getCoord().getLongitudeE6();
			int pos = points.indexOf(point);
			if (points.length() <= 100)
				return "Need more points";
			int start = points.indexOf(" ", pos - 25);
			int end = points.indexOf(" ", pos + 25);

			if (end == -1)
				end = points.indexOf(" ", pos + 1);
			if (end == -1)
				return "Need more points";

			if (start == -1)
				start = points.indexOf(" ", pos - 15);
			if (start == -1)
				return "Need more points";

			query = points.substring(start + 1, end - 1);
			query = query.replaceAll(" ", "");

			String[] arr = query.split(",");
			String temp = "";
			for (int i = 0; i < arr.length; i++) {
				temp += Integer.valueOf(arr[i]) / 1e6 + ",";
			}
			query = temp.substring(0, temp.length() - 1);
			Log.d("xxx", query);
		}

		// Food search
		// List<Food> foods = null;
		// FoodJsonParser jsonParser = new FoodJsonParser(context);
		// foods = jsonParser.parseFoods(jsonParser.retrieveStream(Tools
		// .getFoodLineURL(query, Tools.TRAJECTORY_DISTANCE_TO_LINES)));
		//
		// int numFoods = 0;
		// if (foods != null) {
		// MapBrowserActivity.setupFoods(foods);
		// numFoods = foods.size();
		// }

		// Pharmacy
		List<Buildings> buildings = null;
		BuildingsJsonParser jsonBuildingParser = new BuildingsJsonParser(
				context);
		buildings = jsonBuildingParser.parseBuildings(jsonBuildingParser
				.retrieveStream(Tools.getBuildingLineURL("pharmacy", query,
						Tools.TRAJECTORY_DISTANCE_TO_LINES)));
		int numPharmacys = 0;
		if (buildings != null) {
			MapBrowserActivity.setupBuildings(buildings, "pharmacy");
			numPharmacys = buildings.size();
		}
		
		// Park
		buildings = jsonBuildingParser.parseBuildings(jsonBuildingParser
				.retrieveStream(Tools.getBuildingLineURL("park", query,
						Tools.TRAJECTORY_DISTANCE_TO_LINES)));
		int numParks = 0;
		if (buildings != null) {
			MapBrowserActivity.setupBuildings(buildings, "park");
			numParks = buildings.size();
		}
		
		// Public transport
		buildings = jsonBuildingParser.parseBuildings(jsonBuildingParser
				.retrieveStream(Tools.getBuildingLineURL("public_transport", query,
						Tools.TRAJECTORY_DISTANCE_TO_LINES)));

		int numPublicTransports = 0;
		if (buildings != null) {
			MapBrowserActivity.setupBuildings(buildings, "public_transport");
			numPublicTransports = buildings.size();
		}
		sb.append("<p><b>Pharmacy: </b>" + numPharmacys + "</p>");
		sb.append("<p><b>Public transport: </b>" + numPublicTransports + "</p>");
		sb.append("<p><b>Park: </b>" + numParks + "</p>");

		return sb.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle(FB_BLUE);
		setUpWebView();
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		addContentView(mContent,
				display.getWidth() < display.getHeight() ? DEFAULT_PORTRAIT
						: DEFAULT_LANDSCAPE);
	}

	private void setUpTitle(int color) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Drawable icon = Drawable.createFromStream(getClass().getClassLoader()
				.getResourceAsStream(FB_ICON), FB_ICON);
		mTitle = new TextView(getContext());
		mTitle.setText("Trajectory stop");
		mTitle.setTextColor(Color.BLACK);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);

		mTitle.setBackgroundColor(color);// FB_BLUE);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		mContent.addView(mTitle);
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(true);
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadData(eventHTML, "text/html", null);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("TrajectoryStopDialog", "onTouchEvent is happening.");
		this.dismiss();
		return super.onTouchEvent(event);
	}

}
