package edu.usc.imsc.activities;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.usc.imsc.R;

/**
 * @author linghu
 *
 */
public abstract class AbstractActivity extends Activity{

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.info_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/**
	 * Performs a refresh of the data that this activity is bound to
	 * 
	 * @param fromCache
	 *            if true, refresh will be performed on client-side cache
	 *            (faster, but may be stale), otherwise, the activity will
	 *            perform a server request to get updated data
	 */
	public abstract void refresh(boolean fromCache);
}
