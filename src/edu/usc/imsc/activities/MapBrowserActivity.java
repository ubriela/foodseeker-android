package edu.usc.imsc.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.HttpClient;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ZoomButton;
import android.widget.ZoomButtonsController;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ZoomButtonsController.OnZoomListener;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.usc.imsc.R;
import edu.usc.imsc.SAX.handler.PlaceHandler;
import edu.usc.imsc.camera.CameraActivity;
import edu.usc.imsc.camera.ImageUploadActivity;
import edu.usc.imsc.facebook.DataStorage;
import edu.usc.imsc.facebook.FacebookActivity;
import edu.usc.imsc.facebook.FacebookProfile;
import edu.usc.imsc.food.Food;
import edu.usc.imsc.food.FoodJsonParser;
import edu.usc.imsc.food.FoodOverlay;
import edu.usc.imsc.food.FoodOverlayItem;
import edu.usc.imsc.listener.BuildingOverlayListener;
import edu.usc.imsc.listener.EventOverlayListener;
import edu.usc.imsc.services.SaveLocationIntentService;
import edu.usc.imsc.spatial.Building;
import edu.usc.imsc.spatial.BuildingOverlay;
import edu.usc.imsc.spatial.BuildingOverlayItem;
import edu.usc.imsc.spatial.building.BuildingsOverlay;
import edu.usc.imsc.spatial.building.BuildingsOverlayItem;
import edu.usc.imsc.spatial.Place;
import edu.usc.imsc.spatial.PlaceHolder;
import edu.usc.imsc.spatial.building.Buildings;
import edu.usc.imsc.trajectory.HistoryLocation;
import edu.usc.imsc.trajectory.TrajectoryApp;
import edu.usc.imsc.trajectory.TrajectoryJsonParser;
import edu.usc.imsc.trajectory.TrajectoryPath;
import edu.usc.imsc.trajectory.TrajectoryPathOverlay;
import edu.usc.imsc.trajectory.TrajectoryStop;
import edu.usc.imsc.trajectory.TrajectoryStopOverlay;
import edu.usc.imsc.util.Tools;
import edu.usc.imsc.util.Tools.QueryType;

public class MapBrowserActivity extends MapActivity implements
		BuildingOverlayListener, EventOverlayListener, OnZoomListener,
		OnClickListener, OnTouchListener {
	// , OnSeekBarChangeListener, OnDoubleTapListener, OnGestureListener
	private EditText selectedLocationField;
	private String locationName;
	private int locationId;
	private boolean selectedLocation;

	private boolean selectedEvent;
	private String eventTitle;
	private int eventId;

	static private BuildingOverlay buildingOverlay;
	static private BuildingsOverlay parksOverlay;
	static private BuildingsOverlay pharmacyOverlay;
	private static BuildingsOverlay public_transportsOverlay;
	public static FoodOverlay foodOverlay;
	private SharedPreferences preferences;

	public static MyLocationOverlay myLocationOverlay;

	HttpClient httpClient = AndroidHttpClient.newInstance("Android-iCampus");

	private Handler handler;
	static private Context context;

	// hientt
	boolean isBuildingSelected = false;
	static boolean isFoodSelected = false;
	boolean isEventSelected = false;
	boolean isTrajectorySelected = false;

	private static final int DIALOG_YES_NO_MESSAGE = 1;
	private static final int DIALOG_YES_NO_LONG_MESSAGE = 2;
	private static final int DIALOG_LIST = 3;
	private static final int DIALOG_PROGRESS = 4;
	private static final int DIALOG_SINGLE_CHOICE = 5;
	private static final int DIALOG_MULTIPLE_CHOICE = 6;
	private static final int DIALOG_TEXT_ENTRY = 7;
	private static final int DIALOG_MULTIPLE_CHOICE_CURSOR = 8;

	private static boolean[] mapLayersSelected = { false, false, false };
	public static SharedPreferences sharedPreferences;

	public static boolean isSpatialQuery = false;
	public static QueryType queryType = QueryType.Rectangle;
	public static Vector<Double> tappedPoints = new Vector<Double>();
	public static List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

	ZoomButtonsController zoomButtonController = null;
	ZoomControls zoomControls = null;
	ZoomButton zoomButton = null;
	public static Button fb_button = null;

	public static ArrayList<String> listBuildings = null;
	AutoCompleteTextView buildingTextView = null;
	ImageView buildingSearchImageView = null;

	protected final static int MAP_LAYER_REQUEST_CODE = 1;
	protected final static int STILL_IMAGE_CAMERA_REQUEST_CODE = 2;
	protected final static int VIDEO_CAMERA_REQUEST_CODE = 3;
	protected final static int IMAGE_PICK_REQUEST_CODE = 4;
	protected final static int SETTINGS_REQUEST_CODE = 5;
	protected final static int FOOD_TABS_REQUEST_CODE = 6;
	public static MapView map;
	public static TrajectoryPath trajectoryPath;

	// private TrajectoryStopOverlay trajectoryStopOverlay;
	// private TrajectoryPathOverlay trajectoryPathOverlay;

	// private SeekBar seekBar;
	// private GestureDetector gestureDetector;
	// long lasttime = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.locationId = -1;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		context = this;
		handler = new Handler();

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mapview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.map_title);

		OnClickListener searchImageListener = new OnClickListener() {
			public void onClick(View v) {
				buildingTextView.getText().clear();
				buildingTextView.setVisibility((buildingTextView
						.getVisibility() == View.VISIBLE) ? View.INVISIBLE
						: View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInputFromInputMethod(
						buildingTextView.getWindowToken(), 1);
				// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			}
		};

		buildingSearchImageView = (ImageView) findViewById(R.id.map_title_search);
		buildingSearchImageView.setOnClickListener(searchImageListener);

		this.setupMapView();

		// found on
		// http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled

		final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Yout GPS seems to be disabled, do you want to enable it?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										@SuppressWarnings("unused") final DialogInterface dialog,
										@SuppressWarnings("unused") final int id) {
									startActivity(new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										@SuppressWarnings("unused") final int id) {
									dialog.cancel();
								}
							});
			final AlertDialog alert = builder.create();
			alert.show();
		}

		// end found on
		// http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled

		sharedPreferences = getPreferences(MODE_APPEND);
		if (sharedPreferences.getBoolean("USC buildings", false)) {
			new BuildingSetupTask().execute(this);
			isBuildingSelected = true;
			this.isLayerSelections[0] = true;
		}

		if (sharedPreferences.getBoolean("Foods", false)) {
			this.setupFoods();
			isFoodSelected = true;
			this.isLayerSelections[1] = true;
		}

		if (sharedPreferences.getBoolean("Trajectory", false)) {
			this.setupTrajectory();
			isTrajectorySelected = true;
			this.isLayerSelections[2] = true;
		}

		fb_button = (Button) findViewById(R.id.FacebookButton);

		SharedPreferences settings = getSharedPreferences(
				Tools.PREFERENCE_FILE, MODE_PRIVATE);
		if (settings.getBoolean("isLogin", false)) {
			Log.d(Tools.TAG, "login " + settings.getBoolean("isLogin", false));
			DataStorage.setLoggedIn(true);
			fb_button.setVisibility(View.VISIBLE);
			FacebookProfile fb_profile = new FacebookProfile(
					settings.getString("id", ""),
					settings.getString("name", ""));
			fb_profile.setEmail(settings.getString("email", ""));
			fb_profile.setFirstName(settings.getString("firstname", ""));
			fb_profile.setLastName(settings.getString("lastname", ""));
			DataStorage.setMe(fb_profile);
		}

		// Search building
		if (listBuildings == null) {
			setupBuildingSearch();
		}

		// Service to save location
		// startService(new Intent(this, SaveLocationIntentService.class));

		// Seek bar
		// seekBar = (SeekBar) findViewById(R.id.seek);
		// seekBar.setOnSeekBarChangeListener(this);
		//
		// gestureDetector = new GestureDetector((OnGestureListener) this);
		// gestureDetector.setOnDoubleTapListener((OnDoubleTapListener) this);
	}

	private void setupTrajectory() {
		isTrajectorySelected = true;
		if (isTrajectorySelected) {
			Log.d("xxx", "Setup Trajectory");
			TrajectoryApp trajectoryApp = TrajectoryApp.getInstance(context);
			setupTrajectoryStop(trajectoryApp);
			setupTrajectoryPath(trajectoryApp);
		}
	}

	// private TrajectoryStopOverlay trajectoryStopOverlay;
	// private TrajectoryPathOverlay trajectoryPathOverlay;

	private void setupTrajectoryPath(TrajectoryApp trajectoryApp) {
		// TODO Auto-generated method stub
		trajectoryPath = trajectoryApp.getTrajectoryPath();
		List<GeoPoint> coords = trajectoryPath.getCoords();
		drawPath(coords, Color.BLUE);// R.color.solid_red);//android.R.color.darker_gray);
	}

	private void setupTrajectoryStop(TrajectoryApp trajectoryApp) {
		Drawable trajectoryStopDrawable = getResources().getDrawable(
				R.drawable.stop_16);
		trajectoryPath = trajectoryApp.getTrajectoryPath();
		TrajectoryStopOverlay trajectoryStopOverlay = new TrajectoryStopOverlay(
				trajectoryStopDrawable, this);

		// Iterator it = trajectoryPath.getStops().iterator();
		// while (it.hasNext()) {
		// TrajectoryStop stop = (TrajectoryStop) it.next();
		// trajectoryStopOverlay.addOverlay(stop);
		// }
		for (int routeid : trajectoryPath.getStops().keySet()) {
			trajectoryStopOverlay.addOverlay(trajectoryPath.getStops().get(
					routeid));
		}

		map.getOverlays().add(trajectoryStopOverlay);
		map.invalidate();
	}

	private void drawPath(List<GeoPoint> geoPoints, int color) {
		TrajectoryPathOverlay trajectoryPathOverlay = new TrajectoryPathOverlay(
				geoPoints, color);
		map.getOverlays().add(trajectoryPathOverlay);
		map.invalidate();
	}

	private void setupBuildingSearch() {
		// TODO Auto-generated method stub
		listBuildings = new ArrayList<String>();

		SAXParser parser;
		PlaceHandler handler = null;
		if (PlaceHolder.getInstance().getPlaces().isEmpty()) {
			try {
				parser = SAXParserFactory.newInstance().newSAXParser();
				handler = new PlaceHandler();
				InputStream is = this.getResources().openRawResource(
						R.raw.uscbuildings_new);
				parser.parse(is, handler);
				PlaceHolder.getInstance().setPlaces(handler.getPlaces());
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Building place : PlaceHolder.getInstance().getPlaces()) {
			if (place.getName() != null)
				listBuildings.add(place.getName().trim());
			if (place.getShortName() != null)
				listBuildings.add(place.getShortName().trim());
			// if (place.getDepartmentName() != null)
			// listBuildings.add(place.getDepartmentName().trim());
			// listBuildings.add(place.getAddress());
			// listBuildings.add(place.getDepartmentLocation());
		}

		buildingTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_map);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, listBuildings);
		buildingTextView.setAdapter(adapter);

		buildingTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					Object test = parent.getAdapter().getItem(position);
					if (adapter.getItem(position) != null) {
						Log.d(Tools.TAG, adapter.getItem(position));
						Building building = null;

						for (Building place : PlaceHolder.getInstance()
								.getPlaces()) {
							if (place.getName().trim()
									.equals(adapter.getItem(position))) {
								building = place;
								break;
							}
							if (place.getShortName().trim()
									.equals(adapter.getItem(position))) {
								building = place;
								break;
							}
						}

						// navigate to the building
						if (building.getLocation() != null) {

							map.getController().animateTo(
									building.getLocation());
							BuildingOverlayItem item = new BuildingOverlayItem(
									building);

							// Setup builing mark overlay
							Drawable buildingMark = context.getResources()
									.getDrawable(R.drawable.yellowpin);
							buildingMark.setBounds(0, 0,
									buildingMark.getIntrinsicWidth(),
									buildingMark.getIntrinsicHeight());
							BuildingOverlay overlay = new BuildingOverlay(
									buildingMark, map, context);
							// overlay.addBuildingOverlayListener();
							overlay.addOverlay(item);
							map.getOverlays().add(overlay);
							map.invalidate();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				buildingTextView.setVisibility(View.INVISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(buildingTextView.getWindowToken(),
						0);

			}
		});
	}

	private void setupMapView() {
		map = (MapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		zoomToUSC();

		layerSelections[0] = "USC buildings";
		layerSelections[1] = "Foods";
		layerSelections[2] = "Trajectory";

		myLocationOverlay = new MyLocationOverlay(this, this.map);
		map.getOverlays().add(myLocationOverlay);

		zoomButtonController = map.getZoomButtonsController();
		zoomButtonController.setOnZoomListener(this);
		map.setOnTouchListener(this);
	}

	/**
	 * Display USC's building
	 */

	private class BuildingSetupTask extends
			AsyncTask<MapBrowserActivity, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(context);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			Dialog.setMessage("Please wait ...");
			Dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (Dialog.isShowing()) {
					Dialog.dismiss();
				}
				// do your Display and data setting operation here
			} catch (Exception e) {

			}

			if (buildingOverlay != null) {
				map.getOverlays().add(buildingOverlay);
				map.invalidate();
			}
		}

		@Override
		protected Void doInBackground(MapBrowserActivity... params) {
			// TODO Auto-generated method stub
			isBuildingSelected = true;
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.buildings32);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			if (buildingOverlay == null) {
				buildingOverlay = new BuildingOverlay(drawable, map, context);
				buildingOverlay.addBuildingOverlayListener(params[0]);
				try {
					if (PlaceHolder.getInstance().getPlaces().isEmpty()) {
						SAXParser parser = SAXParserFactory.newInstance()
								.newSAXParser();
						PlaceHandler handler = new PlaceHandler();
						InputStream is = context.getResources()
								.openRawResource(R.raw.uscbuildings_new);
						parser.parse(is, handler);
						PlaceHolder.getInstance()
								.setPlaces(handler.getPlaces());
					}
					for (Building place : PlaceHolder.getInstance().getPlaces()) {
						BuildingOverlayItem oi = new BuildingOverlayItem(place);
						buildingOverlay.addOverlay(oi);
						if (place.getId() == locationId) {
							buildingOverlay.setSelectedItem(oi);
							selectedLocation = true;
							locationName = buildingOverlay
									.getSelectedPlaceName();
						}
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public void setupFoods() {
		if (isFoodSelected == false)
			return;

		// We will not display food if zoom level is small
		if (map.getZoomLevel() < Tools.ZOOM_LEVEL_THRESHOLD)
			return;

		isFoodSelected = true;
		Drawable drawable = this.getResources().getDrawable(R.drawable.food_32);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		if (foodOverlay != null) {
			map.getOverlays().remove(foodOverlay);
			foodOverlay.clear();
		}

		foodOverlay = new FoodOverlay(drawable, map, this);
		foodOverlay.addBuildingOverlayListener(this);

		FoodJsonParser jsonParser = new FoodJsonParser(this);

		Vector<Double> screenCoords = new Vector<Double>();

		// Get screen coords
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		GeoPoint p1 = this.getMap().getProjection().fromPixels(0, 0);

		GeoPoint p2 = this.getMap().getProjection().fromPixels(width, height);
		screenCoords.add(p1.getLatitudeE6() / 1E6);
		screenCoords.add(p1.getLongitudeE6() / 1E6);
		screenCoords.add(p2.getLatitudeE6() / 1E6);
		screenCoords.add(p2.getLongitudeE6() / 1E6);

		List<Food> foods = jsonParser.parseFoods(jsonParser
				.retrieveStream(Tools
						.getRectangleSpatialQueryService(screenCoords)));

		// Log.d(Tools.TAG, "Food size: " + String.valueOf(foods.size()));
		if (foods != null) {
			for (Food food : foods) {
				Place place = new Place(Integer.valueOf(String.valueOf(food
						.getPid())), food.getTitle(), Double.valueOf(food
						.getLat()), Double.valueOf(food.getLon()));
				// Log.d("Place", String.valueOf(place.getLongitude()));
				// Log.d("Place", String.valueOf(place.getLatitude()));
				FoodOverlayItem oi = new FoodOverlayItem(place);
				oi.setFood(food);
				foodOverlay.addOverlay(oi);
				if (place.getId() == locationId) {
					foodOverlay.setSelectedItem(oi);
					selectedLocation = true;
					locationName = foodOverlay.getSelectedPlaceName();
				}
			}

			map.getOverlays().add(foodOverlay);
			map.invalidate();
		}
	}

	public static void setupBuildings(List<Buildings> buildings, String type) {
		// TODO Auto-generated method stub
		if (map.getZoomLevel() < Tools.ZOOM_LEVEL_THRESHOLD)
			return;

		Drawable drawable = null;

		if (type == "pharmacy") {
			drawable = context.getResources().getDrawable(
					R.drawable.pharmacy_64);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());

			if (pharmacyOverlay != null) {
				map.getOverlays().remove(pharmacyOverlay);
				pharmacyOverlay.clear();
			}

			pharmacyOverlay = new BuildingsOverlay(drawable, map, context);

			if (buildings != null) {

				for (Buildings building : buildings) {
					Place place = new Place(Integer.valueOf(String
							.valueOf(building.getId())), building.getName(),
							Double.valueOf(building.getLat()),
							Double.valueOf(building.getLon()));

					BuildingsOverlayItem oi = new BuildingsOverlayItem(place);
					pharmacyOverlay.addOverlay(oi);
				}

				map.getOverlays().add(pharmacyOverlay);
				map.invalidate();
			}
		} else if (type == "park") {
			drawable = context.getResources().getDrawable(R.drawable.park_64);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());

			if (parksOverlay != null) {
				map.getOverlays().remove(parksOverlay);
				parksOverlay.clear();
			}

			parksOverlay = new BuildingsOverlay(drawable, map, context);

			if (buildings != null) {

				for (Buildings building : buildings) {
					Place place = new Place(Integer.valueOf(String
							.valueOf(building.getId())), building.getName(),
							Double.valueOf(building.getLat()),
							Double.valueOf(building.getLon()));

					BuildingsOverlayItem oi = new BuildingsOverlayItem(place);
					parksOverlay.addOverlay(oi);
				}

				map.getOverlays().add(parksOverlay);
				map.invalidate();
			}
		} else if (type == "public_transport") {
			drawable = context.getResources().getDrawable(
					R.drawable.public_transport_64);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());

			if (public_transportsOverlay != null) {
				map.getOverlays().remove(public_transportsOverlay);
				public_transportsOverlay.clear();
			}

			public_transportsOverlay = new BuildingsOverlay(drawable, map,
					context);

			if (buildings != null) {

				for (Buildings building : buildings) {
					Place place = new Place(Integer.valueOf(String
							.valueOf(building.getId())), building.getName(),
							Double.valueOf(building.getLat()),
							Double.valueOf(building.getLon()));

					BuildingsOverlayItem oi = new BuildingsOverlayItem(place);
					public_transportsOverlay.addOverlay(oi);
				}

				map.getOverlays().add(public_transportsOverlay);
				map.invalidate();
			}
		}

	}

	public static void setupFoods(List<Food> foods) {

		// We will not display food if zoom level is small
		if (map.getZoomLevel() < Tools.ZOOM_LEVEL_THRESHOLD)
			return;

		Drawable drawable = context.getResources().getDrawable(
				R.drawable.pharmacy_64);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		if (foodOverlay != null) {
			map.getOverlays().remove(foodOverlay);
			foodOverlay.clear();
		}

		foodOverlay = new FoodOverlay(drawable, map, context);

		if (foods != null) {
			for (Food food : foods) {
				Place place = new Place(Integer.valueOf(String.valueOf(food
						.getPid())), food.getTitle(), Double.valueOf(food
						.getLat()), Double.valueOf(food.getLon()));
				FoodOverlayItem oi = new FoodOverlayItem(place);
				oi.setFood(food);
				foodOverlay.addOverlay(oi);
			}

			map.getOverlays().add(foodOverlay);
			map.invalidate();
		}
	}

	public void searchSpatialFood(Tools.QueryType spatialQueryType) {
		Drawable drawable = this.getResources().getDrawable(R.drawable.food_32);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		isFoodSelected = false;
		isLayerSelections[1] = false;

		if (foodOverlay != null) {
			map.getOverlays().remove(foodOverlay);
			foodOverlay.clear();
		}

		foodOverlay = new FoodOverlay(drawable, map, this);
		foodOverlay.addBuildingOverlayListener(this);
		FoodJsonParser jsonParser = new FoodJsonParser(this);

		List<Food> foods = null;

		switch (spatialQueryType) {
		case Rectangle:
			foods = jsonParser.parseFoods(jsonParser.retrieveStream(Tools
					.getRectangleSpatialQueryService(tappedPoints)));
			break;
		case Polygon:
			Log.d(Tools.TAG, Tools.getPolygonSpatialQueryService(geoPoints));
			foods = jsonParser.parseFoods(jsonParser.retrieveStream(Tools
					.getPolygonSpatialQueryService(geoPoints)));
			break;
		case Circle:
			foods = jsonParser.parseFoods(jsonParser.retrieveStream(Tools
					.getCircleSpatialQueryService(geoPoints)));
			break;
		case KNN:
			Log.d(Tools.TAG, Tools.getKNNSpatialQueryService(tappedPoints));
			foods = jsonParser.parseFoods(jsonParser.retrieveStream(Tools
					.getKNNSpatialQueryService(tappedPoints)));
			break;
		}

		Log.d(Tools.TAG, "Food size: " + String.valueOf(foods.size()));
		for (Food food : foods) {
			Place place = new Place(Integer.valueOf(String.valueOf(food
					.getPid())), food.getTitle(),
					Double.valueOf(food.getLat()),
					Double.valueOf(food.getLon()));
			// Log.d("Place", String.valueOf(place.getLongitude()));
			// Log.d("Place", String.valueOf(place.getLatitude()));
			FoodOverlayItem oi = new FoodOverlayItem(place);
			foodOverlay.addOverlay(oi);
			if (place.getId() == locationId) {
				foodOverlay.setSelectedItem(oi);
				selectedLocation = true;
				locationName = foodOverlay.getSelectedPlaceName();
			}
		}

		map.getOverlays().add(foodOverlay);
		map.invalidate();

		MapBrowserActivity.isSpatialQuery = false;
		if (tappedPoints != null)
			MapBrowserActivity.tappedPoints.clear();
		if (geoPoints != null)
			MapBrowserActivity.geoPoints.clear();
	}

	/**
	 * show a list of layer types, ex Street view, Sattelite
	 */
	private void show_google_map_layers() {
		/* Display a list of checkboxes */
		showDialog(DIALOG_MULTIPLE_CHOICE);
	}

	private void show_spatial_search() {
		showDialog(DIALOG_LIST);
	}

	private void show_about() {
		Intent i = new Intent(this, AboutActivity.class);
		startActivity(i);
	}

	@Override
	public void onPlaceSelectionChange() {
		selectedLocation = true;
		locationName = buildingOverlay.getSelectedPlaceName();
		locationId = buildingOverlay.getSelectedPlaceId();
	}

	public void onOkClick(View v) {

	}

	public void onCancelClick(View v) {

	}

	public void locationSelectionFieldOnClick(View v) {

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showDialog(DIALOG_YES_NO_MESSAGE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.my_location:
			Log.i(Tools.TAG, "my location menu click.");
			myLocationOverlay = new MyLocationOverlay(this, map);
			myLocationOverlay.enableMyLocation();
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					MapController mapController = map.getController();
					GeoPoint current = myLocationOverlay.getMyLocation();
					if (current != null) {

						// Start animating the map towards the given point
						mapController.animateTo(current);
						mapController.setZoom(19);
					} else {
						Log.d(Tools.TAG, "current GPS reading is null!");
					}
				}
			});
			map.getOverlays().add(myLocationOverlay);
			map.invalidate();
			break;

		case R.id.social:
			show_info();
			break;
		case R.id.search:
			show_spatial_search();
			break;
		case R.id.camera:
			show_camera();
			break;
		case R.id.about:
			show_about();
			break;
		case R.id.layers:
			show_google_map_layers();
			break;
		case R.id.exit:
			Log.i(Tools.TAG, "iCampus-clicked on exit!");
			System.exit(0);
			// finish(); //this does not stop timer tasks
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void show_camera() {
		// TODO Auto-generated method stub
		if (DataStorage.isLoggedIn()) {
			Intent i = new Intent().setClass(this, ImageUploadActivity.class);
//			Intent i = new Intent().setClass(this, CameraActivity.class);
			startActivity(i);
		} else {
			new AlertDialog.Builder(this)
					.setMessage("You need login to view status!")
					.setTitle("Status").setCancelable(true).show();
		}
	}

	private void show_info() {
		Intent intent = new Intent().setClass(this, SocialActivity.class);
		this.startActivity(intent);
	}

	/**
	 * this method decides which layers are displayed
	 */

	/*
	 * 
	 * 
	 * case MAP_LAYER_REQUEST_CODE: // resultCode is returned from //
	 * LayerSelectionListActivity class if (resultCode == Activity.RESULT_OK) {
	 * Log.d(Tools.TAG, "RESULT_OK"); // get a list ofvisible layers returned
	 * from // LayerSelectionListActivity layerSelections =
	 * data.getExtras().getStringArray( Tools.MAP_LAYER_PARAM);
	 * isLayerSelections = data.getExtras().getBooleanArray(
	 * Tools.MAP_LAYER_VALUE);
	 * 
	 * // save settings to preferences // preferences. } else if (resultCode ==
	 * Activity.RESULT_CANCELED) { Log.d(Tools.TAG, "RESULT_CANCELED"); }
	 * Log.d(Tools.TAG, layerSelections.toString()); // decide which layers to
	 * display ... break;
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d("xyz", "Here");

		switch (requestCode) {
		case MAP_LAYER_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				Log.d(Tools.TAG, "RESULT_OK");

				// get the new layer selections
				layerSelections = data.getExtras().getStringArray(
						Tools.MAP_LAYER_PARAM);
				isLayerSelections = data.getExtras().getBooleanArray(
						Tools.MAP_LAYER_VALUE);

				map.getOverlays().clear();
				map.invalidate();

				isBuildingSelected = false;
				isFoodSelected = false;
				isEventSelected = false;
				isTrajectorySelected = false;
				sharedPreferences = getPreferences(MODE_PRIVATE);
				SharedPreferences.Editor prefsEditor = preferences.edit();
				prefsEditor.putBoolean("USC buildings", false);
				// prefsEditor.putBoolean("Trams", false);
				prefsEditor.putBoolean("USC events", false);
				prefsEditor.putBoolean("Trajectory", false);

				for (int i = 0; i < layerSelections.length; i++) {
					if (isLayerSelections[i]) {
						if (layerSelections[i].equals("USC buildings")) {
							if (isBuildingSelected == false) {
								new BuildingSetupTask().execute(this);
							}

							prefsEditor.putBoolean("USC buildings", true);
						} else if (layerSelections[i].equals("Foods")) {
							if (layerSelections[i].equals("Foods")) {
								if (isFoodSelected == false) {
									this.setupFoods();
									isFoodSelected = true;
								}
							}
							prefsEditor.putBoolean("Foods", true);
						} else if (layerSelections[i].equals("Trajectory")) {
							if (layerSelections[i].equals("Trajectory")) {
								if (isTrajectorySelected == false) {
									this.setupTrajectory();
								}
							}
							prefsEditor.putBoolean("Trajectory", true);
						}
					}

				}
				prefsEditor.commit();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.d(Tools.TAG, "RESULT_CANCELED");
			}
			Log.d(Tools.TAG, layerSelections.toString());
			// decide which layers to display
			break;

		case SETTINGS_REQUEST_CODE:
			break;
		case FOOD_TABS_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				Log.d("xyz", "Here");
				String pid = "";
				String title = "";
				String time = "";
				String rating = "";
				String place = "";
				String photo = "";
				String lat = "";
				String lon = "";
				if (data.getExtras().getString("food_pid") != null)
					pid = data.getExtras().getString("food_pid");
				if (data.getExtras().getString("food_title") != null)
					title = data.getExtras().getString("food_title");
				if (data.getExtras().getString("food_lat") != null)
					lat = data.getExtras().getString("food_lat");
				if (data.getExtras().getString("food_lon") != null)
					lon = data.getExtras().getString("food_lon");
				if (data.getExtras().getString("food_rating") != null)
					rating = data.getExtras().getString("food_rating");
				if (data.getExtras().getString("food_place") != null)
					place = data.getExtras().getString("food_place");
				if (data.getExtras().getString("food_photo") != null)
					photo = data.getExtras().getString("food_photo");

				Log.d("xyz", "1");
				Place pl = new Place(Integer.valueOf(pid), title,
						Double.valueOf(lat), Double.valueOf(lon));
				Food food = new Food(time, title, rating, place, photo, lon,
						lat);
				Log.d("xyz", "2");
				FoodOverlayItem item = new FoodOverlayItem(pl);
				item.setFood(food);

				// Setup food mark overlay
				Drawable foodMark = context.getResources().getDrawable(
						R.drawable.food_64);
				foodMark.setBounds(0, 0, foodMark.getIntrinsicWidth(),
						foodMark.getIntrinsicHeight());
				FoodOverlay overlay = new FoodOverlay(foodMark, map, context);
				overlay.addOverlay(item);
				Log.d("xyz", String.valueOf((int) (Double.valueOf(lat) * 1E6)));
				Log.d("xyz", String.valueOf((int) (Double.valueOf(lon) * 1E6)));

				map.getController().animateTo(
						new GeoPoint((int) (Double.valueOf(lat) * 1E6),
								(int) (Double.valueOf(lon) * 1E6)));
				map.getOverlays().add(overlay);
				map.invalidate();
			}
			break;
		default:
			break;

		}
	}

	/**
	 * display MapLayers dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DIALOG_MULTIPLE_CHOICE:
			return new AlertDialog.Builder(MapBrowserActivity.this)
					.setIcon(R.drawable.ic_popup_layers)
					.setTitle(R.string.dialog_multi_choice)
					.setMultiChoiceItems(R.array.mapLayerTypes,
							mapLayersSelected,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {

									/* User clicked on a check box do some stuff */
									mapLayersSelected[whichButton] = isChecked;
								}
							})
					.setPositiveButton(R.string.dialog_multi_choice_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Yes so do some stuff */
									map.setStreetView(mapLayersSelected[0]);
									map.setSatellite(mapLayersSelected[1]);
									map.setTraffic(mapLayersSelected[2]);
								}
							})
					.setNegativeButton(R.string.dialog_multi_choice_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked No so do some stuff */
									// do nothing!
								}
							}).create();
		case DIALOG_LIST:
			return new AlertDialog.Builder(MapBrowserActivity.this)
					.setTitle("Select a query type")
					.setItems(Tools.queryTypes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									Toast.makeText(getApplicationContext(),
											Tools.queryTypes[item],
											Toast.LENGTH_SHORT).show();
									isSpatialQuery = true;
									queryType = QueryType
											.valueOf((String) Tools.queryTypes[item]);
								}
							}).create();
		case DIALOG_YES_NO_MESSAGE:
			return new AlertDialog.Builder(MapBrowserActivity.this)
					.setTitle("Do you really want to quit?")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// User clicked Cancel so do some stuff
									System.out.println("cancel clicked.");
								}
							}).create();

		}

		if (queryType == QueryType.KNN) {

		}
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(Tools.TAG, "map activity on pause");
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(Tools.TAG, "map activity onResume");

		myLocationOverlay.enableMyLocation();
	}

	private boolean isNetworkConnected() {
		NetworkInfo activeNetworkInfo = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();// .getAllNetworkInfo();
			activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				return activeNetworkInfo.isConnected();
			}
			// NetworkInfo networkInfo =
			// conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeNetworkInfo != null;
	}

	@Override
	public void onVisibilityChanged(boolean visible) {

	}

	@Override
	public void onZoom(boolean isZoomIn) {
		// TODO Auto-generated method stub
		Log.d(Tools.TAG, "Zoom level: " + String.valueOf(map.getZoomLevel()));
		if (isZoomIn) {
			map.getController().zoomIn();
			setupFoods();
		} else {
			map.getController().zoomOut();
			setupFoods();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		setupFoods();
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// The user took their finger off the map,
			// they probably just moved it to a new place.
			setupFoods();
			break;
		case MotionEvent.ACTION_MOVE:
			// The user is probably moving the map.
			setupFoods();
			break;
		// case MotionEvent.ACTION_DOWN:
		// if (event.getEventTime() - lasttime < 1000) {
		// seekBar.setVisibility((seekBar.getVisibility() == View.VISIBLE) ?
		// View.INVISIBLE
		// : View.VISIBLE);
		// }
		// lasttime = event.getEventTime();
		// return true;
		}

		// Return false so that the map still moves.
		return false;
	}

	/**
	 * @param v
	 */
	public void showFacebook(View v) {
		Log.d(Tools.TAG, "buttonFacebook");
		this.startActivity(new Intent(this, FacebookActivity.class));
	}

	public void showFoodTabs(View v) {
		Log.d(Tools.TAG, "foodTabs");
		Intent i = new Intent(this, FoodTabsActivity.class);
		this.startActivityForResult(i, FOOD_TABS_REQUEST_CODE);
	}

	@Override
	public void onEventSelectionChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public MapView getMap() {
		return map;
	}

	protected static String[] layerSelections = new String[Tools.LAYER_NUMBER];
	protected static boolean[] isLayerSelections = new boolean[Tools.LAYER_NUMBER];

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);

		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (myLocationOverlay != null)
			myLocationOverlay.disableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.removeItem(R.id.refresh_taste);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Event handler for zooming to USC campus on the map
	 * 
	 * @param v
	 *            the view calling this as an event handler
	 */
	public void zoomToUSC(View v) {
		zoomToUSC();
	}

	protected void zoomToUSC() {
		GeoPoint gp = new GeoPoint(Tools.USC_POINT_LON, Tools.USC_POINT_LAT);
		MapController mapController = map.getController();
		mapController.zoomToSpan(Tools.MAP_ZOOM_TO_SPAN_LON,
				Tools.MAP_ZOOM_TO_SPAN_LAT);
		mapController.setCenter(gp);
	}

	/**
	 * zoom the map to the right level/view to show your current location and
	 * the farthest point
	 * 
	 * @param myPoint
	 * @param furthestPoint
	 */
	protected void zoomToShowPoints(GeoPoint myPoint, GeoPoint farthestPoint) {
		map.getController()
				.zoomToSpan(
						(farthestPoint.getLatitudeE6() > myPoint.getLatitudeE6() ? farthestPoint.getLatitudeE6()
								- myPoint.getLatitudeE6()
								: myPoint.getLatitudeE6()
										- farthestPoint.getLatitudeE6()),
						(farthestPoint.getLongitudeE6() > myPoint
								.getLongitudeE6() ? farthestPoint
								.getLongitudeE6() - myPoint.getLongitudeE6()
								: myPoint.getLongitudeE6()
										- farthestPoint.getLongitudeE6()));
		map.getController().animateTo(
				new GeoPoint(farthestPoint.getLatitudeE6()
						- ((farthestPoint.getLatitudeE6() - myPoint
								.getLatitudeE6()) / 2), farthestPoint
						.getLongitudeE6()
						- ((farthestPoint.getLongitudeE6() - myPoint
								.getLongitudeE6()) / 2)));
	}

	/**
	 * showLayerList is an onClick action on the map's layer button see map.xml
	 * 
	 * @param v
	 */
	public void showLayerList(View v) {
		Intent layerSelection = new Intent(this, LayersSelectionActivity.class);
		layerSelection.putExtra(Tools.MAP_LAYER_PARAM, layerSelections);
		layerSelection.putExtra(Tools.MAP_LAYER_VALUE, isLayerSelections);
		this.startActivityForResult(layerSelection, MAP_LAYER_REQUEST_CODE);
	}

	// public void onProgressChanged(SeekBar seekBar, int progress,
	// boolean fromTouch) {
	//
	// }
	//
	// public void onStartTrackingTouch(SeekBar seekBar) {
	//
	// }
	//
	// public void onStopTrackingTouch(SeekBar seekBar) {
	//
	// }

	// @Override
	// public boolean onDoubleTap(MotionEvent e) {
	// TODO Auto-generated method stub
	// GeoPoint p = map.getProjection().fromPixels((int) e.getX(),
	// (int) e.getY());
	//
	// AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	// dialog.setTitle("Double Tap");
	// dialog.setMessage("Location: " + p.getLatitudeE6() + ", "
	// + p.getLongitudeE6());
	// dialog.show();

	// seekBar.setVisibility((seekBar.getVisibility() == View.VISIBLE) ?
	// View.INVISIBLE
	// : View.VISIBLE);

	// return true;
	// }

	// @Override
	// public boolean onDoubleTapEvent(MotionEvent e) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public boolean onSingleTapConfirmed(MotionEvent e) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public boolean onDown(MotionEvent e) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
	// float arg3) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public void onLongPress(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
	// float arg3) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public void onShowPress(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public boolean onSingleTapUp(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	// return false;
	// }
}
