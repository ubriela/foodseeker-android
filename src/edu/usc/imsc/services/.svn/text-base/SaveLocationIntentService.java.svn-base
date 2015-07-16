package edu.usc.imsc.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.maps.GeoPoint;

import edu.usc.imsc.activities.MapBrowserActivity;
import edu.usc.imsc.facebook.DataStorage;
import edu.usc.imsc.util.Tools;

public class SaveLocationIntentService extends IntentService {
	static GeoPoint lastPoint;
	static boolean isNewSession = true;
	static long period = 10 * 1000;
	
	public SaveLocationIntentService() {
		super("PersonalizedIntentService");
		// TODO Auto-generated constructor stub
		lastPoint = new GeoPoint(34020209, -118285686);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		while (true) {
			synchronized (this) {
				try {
					GeoPoint currentPoint = MapBrowserActivity.myLocationOverlay
							.getMyLocation();
					
					if (Tools.CalculationByDistance(currentPoint, lastPoint) > 2) {
						if (DataStorage.isLoggedIn()) {
							Log.d("SaveLocation: ", Tools.getSaveLocation(
									DataStorage.getMe().getId(), currentPoint, isNewSession));
							Tools.callService(Tools.getSaveLocation(DataStorage
									.getMe().getId(), currentPoint, isNewSession));
							lastPoint = currentPoint;
							isNewSession = false;
						}
					}

					wait(period);
				} catch (Exception e) {
				}
			}
		}
	}
}
