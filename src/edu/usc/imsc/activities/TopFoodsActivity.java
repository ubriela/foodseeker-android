/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usc.imsc.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.usc.imsc.R;
import edu.usc.imsc.food.Food;
import edu.usc.imsc.food.FoodJsonParser;
import edu.usc.imsc.util.Tools;

/**
 * Demonstrates how to write an efficient list adapter. The adapter used in this example binds
 * to an ImageView and to a TextView for each row in the list.
 *
 * To work efficiently the adapter implemented here uses two techniques:
 * - It reuses the convertView passed to getView() to avoid inflating View when it is not necessary
 * - It uses the ViewHolder pattern to avoid calling findViewById() when it is not necessary
 *
 * The ViewHolder pattern consists in storing a data structure in the tag of the view returned by
 * getView(). This data structures contains references to the views we want to bind data to, thus
 * avoiding calls to findViewById() every time getView() is invoked.
 */

/**
 * 
 * @author win7 replace USCEventActivity class
 */
public class TopFoodsActivity extends ListActivity {

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public Bitmap mIcon;
		private ArrayList<Food> foods;

		public EfficientAdapter(Context context, List<Food> foods) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.foods = new ArrayList<Food>(foods);
			// Icons bound to the rows.
			mIcon = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.usclogo);
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return foods.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			RatingBar mSmallRatingBar = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.events, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.title_text = (TextView) convertView
						.findViewById(R.id.food_title_text);
				holder.food_icon = (ImageView) convertView
						.findViewById(R.id.food_icon);
				mSmallRatingBar = (RatingBar) convertView
						.findViewById(R.id.small_ratingbar);
				mSmallRatingBar.setNumStars(5);
				mSmallRatingBar.setProgress(2 * Integer.valueOf(foods.get(
						position).getRating()));

				holder.place_text = (TextView) convertView
						.findViewById(R.id.food_place_text);
				holder.time_text = (TextView) convertView
						.findViewById(R.id.food_time_text);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.title_text.setText(foods.get(position).getTitle().trim());
			if (foods.get(position).getPlace() != null)
				holder.place_text
						.setText(foods.get(position).getPlace().trim());
			if (foods.get(position).getTime() != null)
				holder.time_text.setText(foods.get(position).getTime().trim());
			
			Bitmap mIcon2 = getBitmapFromURL(foods.get(position).getPhoto()
					.trim());

			if (mIcon2 == null) {
				holder.food_icon.setImageBitmap(mIcon);
			} else {
				holder.food_icon.setImageBitmap(mIcon2);
			}

			return convertView;
		}

		static class ViewHolder {
			TextView title_text;
			ImageView food_icon;
			TextView place_text;
			TextView time_text;
			
		}

		public Bitmap getBitmapFromURL(String src) {
			try {
				URL url = new URL(src);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.food_title);

		FoodJsonParser parser = new FoodJsonParser(this);

		GeoPoint current = MapBrowserActivity.myLocationOverlay.getMyLocation();
		if (current == null) {
			current = new GeoPoint(34020209, -118285686);
		}
		String url = Tools.getTopFoodsURL(10);
		List<Food> foods = parser.parseFoods((parser.retrieveStream(url)));

		// Check availability of USCEvent data
		if (foods == null) {
			Toast.makeText(this, "Service is unavailable!", Toast.LENGTH_LONG);
			this.onBackPressed();
			this.onDestroy();
			return;
		}

		eventsinfo = new ArrayList<Food>();
		for (int i = 0; i < foods.size(); i++) {
			eventsinfo.add(foods.get(i));
		}

		setListAdapter(new EfficientAdapter(this, eventsinfo));

		// getListView().setTextFilterEnabled(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent data = new Intent();
		if (eventsinfo.get(position).getLat() != null)
			data.putExtra("food_lat", eventsinfo.get(position).getLat());
		if (eventsinfo.get(position).getLon() != null)
			data.putExtra("food_lon", eventsinfo.get(position).getLon());
		if (eventsinfo.get(position).getPid() != null)
			data.putExtra("food_pid", eventsinfo.get(position).getPid());
		if (eventsinfo.get(position).getTitle() != null)
			data.putExtra("food_title", eventsinfo.get(position).getTitle());
		if (eventsinfo.get(position).getTime() != null)
			data.putExtra("food_time", eventsinfo.get(position).getTime());
		if (eventsinfo.get(position).getPlace() != null)
			data.putExtra("food_place", eventsinfo.get(position).getPlace());
		if (eventsinfo.get(position).getRating() != null)
			data.putExtra("food_rating", eventsinfo.get(position).getRating());
		if (eventsinfo.get(position).getPhoto() != null)
			data.putExtra("food_photo", eventsinfo.get(position).getPhoto());

		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}

		this.onBackPressed();
	}
	
	public List<Food> eventsinfo;
}
