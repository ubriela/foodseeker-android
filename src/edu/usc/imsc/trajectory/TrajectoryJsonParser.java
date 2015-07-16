package edu.usc.imsc.trajectory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import edu.usc.imsc.util.Tools;

public class TrajectoryJsonParser {
	private Context context;

	List<ITrajectoryListener> trajectoryListeners; // do not use currently
	
	public TrajectoryJsonParser(Context context) {
		this.context = context;
		trajectoryListeners = new ArrayList<ITrajectoryListener>();
	}

	public void addTrajectoryListener(ITrajectoryListener trajectoryListener) {
		trajectoryListeners.add(trajectoryListener);
	}

	public void removeTramListener(ITrajectoryListener trajectoryListener) {
		trajectoryListeners.remove(trajectoryListener);
	}

	public void clearTrajectoryListener() {
		trajectoryListeners.clear();
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public List<HistoryLocation> parseHistoryLocation(InputStream inputStream) {
		Gson gson = new Gson();

		HistoryLocation[] trajectory = null;
		List<HistoryLocation> ret = null;
		try {
			Reader reader = new InputStreamReader(inputStream);
			trajectory = (HistoryLocation[]) gson.fromJson(reader, HistoryLocation[].class);

			ret = new ArrayList<HistoryLocation>();
			for (HistoryLocation location : trajectory) {
				Log.i(Tools.TAG, location.toString());
				ret.add(location);
			}
		} catch (Exception ex) {
			Log.w(getClass().getSimpleName(), "", ex);
			return null;
		}

		if (ret != null && ret.size() > 0)
			return ret;
		else
			return null;
	}
}
