/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usc.imsc.facebook;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseDialogListener;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

import edu.usc.imsc.R;
import edu.usc.imsc.activities.MapBrowserActivity;
import edu.usc.imsc.db.DbAdapter;
import edu.usc.imsc.util.Tools;

public class FacebookActivity extends Activity {

	// Your Facebook Application ID must be set before running this example
	// See http://www.facebook.com/developers/createapp.php
	public static final String APP_ID = "221366747930829";

	private LoginButton mLoginButton;
	private TextView mText;
	private Button mRequestButton;
	private Button mPostButton;
	private Button mDeleteButton;
	private Button mUploadButton;

	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	// -------------
	private String postsString = null;
	private String meString = null;
	private String friendsString = null;

	private DbAdapter dbHelper;
	private Cursor cursor;
	private static final int ACTIVITY_CREATE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

		if (APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running this example: see Example.java");
		}

		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.facebook_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.facebook_title);
		GridView gridview = (GridView) findViewById(R.id.fbgridview);
		gridview.setAdapter(new FacebookButtonsAdapter(this));
        
		mLoginButton = (LoginButton) findViewById(R.id.login);
		mText = (TextView) FacebookActivity.this.findViewById(R.id.txt);
		mRequestButton = (Button) findViewById(R.id.requestButton);
		mPostButton = (Button) findViewById(R.id.post);
		mDeleteButton = (Button) findViewById(R.id.deletePostButton);
		mUploadButton = (Button) findViewById(R.id.uploadButton);

		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener(this));
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.init(this, mFacebook);

		mRequestButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mAsyncRunner.request("me", new SampleRequestListener());
			}
		});
		mRequestButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);

		mUploadButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bundle params = new Bundle();
				params.putString("method", "photos.upload");

				URL uploadFileUrl = null;
				try {
					uploadFileUrl = new URL(
							"http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				try {
					HttpURLConnection conn = (HttpURLConnection) uploadFileUrl
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					int length = conn.getContentLength();

					byte[] imgData = new byte[length];
					InputStream is = conn.getInputStream();
					is.read(imgData);
					params.putByteArray("picture", imgData);

				} catch (IOException e) {
					e.printStackTrace();
				}

				mAsyncRunner.request(null, params, "POST",
						new SampleUploadListener(), null);
			}
		});
		mUploadButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);

		mPostButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mFacebook.dialog(FacebookActivity.this, "feed",
						new SampleDialogListener());
			}
		});
		mPostButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);

		fillData();
	}

	// private void createDb() {
	// Intent i = new Intent(this, Fb_DbDetails.class);
	// startActivityForResult(i, ACTIVITY_CREATE);
	// }
	//
	private void fillData() {
//		openFbDb();
//		cursor = dbHelper.fetchAllFriends();
//		startManagingCursor(cursor);
//
//		ArrayList<String> listFriends = new ArrayList<String>();
//		if (cursor.moveToFirst()) {
//			do {
//				listFriends.add(cursor.getString(1));
//			} while (cursor.moveToNext());
//		}
//		if (cursor != null && !cursor.isClosed()) {
//			cursor.close();
//		}
//
//		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_friends);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				R.layout.list_item, listFriends);
//		textView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	public class SampleAuthListener implements AuthListener {
		private Context context = null;

		public SampleAuthListener(FacebookActivity facebookActivity) {
			// TODO Auto-generated constructor stub
			this.context = facebookActivity;
		}

		public void onAuthSucceed() {
			mText.setText("You have logged in! ");
			mRequestButton.setVisibility(View.VISIBLE);
			mUploadButton.setVisibility(View.VISIBLE);
			mPostButton.setVisibility(View.VISIBLE);

			// Add my process
			try {
				postsString = mFacebook.request("me/posts");
				meString = mFacebook.request("me");
				friendsString = mFacebook.request("me/friends");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Log.d("posts", postsString);
			Log.d("me", meString);
			Log.d("friends", friendsString);

			// Get userprofile
			PostEvent.addPostListener(postListener);
			try {
				FacebookProfile user;
				user = getUserProfile();
				mText.setText("Hi " + user.getFirstName() + " "
						+ user.getLastName() + ". You are logged in now.");

			} catch (FacebookError e) {
				Log.e("Log-in", "Cannot get user's name");
				Log.e("FacebookError", e.getMessage());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Get friend list
//			try {
//				getFriendsProfiles();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FacebookError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			// Get recent posts
//			try {
//				getRecentPosts();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FacebookError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			// Save user profile to icampus database
			String full_url = Tools
					.getUserProfileUrlSet(DataStorage.getMe().getFirstName(),
							DataStorage.getMe().getLastName(), DataStorage
									.getMe().getEmail(), DataStorage.getMe()
									.getId());
			Log.d("FacebookActivity: ", full_url);
			Tools.callService(full_url);

			MapBrowserActivity.fb_button.setVisibility(View.VISIBLE);
			DataStorage.setLoggedIn(true);
			
			// Save user profile to persistent storage
			SharedPreferences preferences = getSharedPreferences(Tools.PREFERENCE_FILE, MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = preferences.edit();
			prefsEditor.putBoolean("isLogin", true);
			prefsEditor.putString("id", DataStorage.getMe().getId());
			prefsEditor.putString("firstname", DataStorage.getMe()
					.getFirstName());
			prefsEditor.putString("lastname", DataStorage.getMe()
					.getLastName());
			prefsEditor.putString("name", DataStorage.getMe().getName());
			prefsEditor.putString("email", DataStorage.getMe().getEmail());
			prefsEditor.commit();
			
		}

		public void onAuthFail(String error) {
			mText.setText("Login Failed: " + error);
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			mText.setText("Logging out...");
		}

		public void onLogoutFinish() {
			mText.setText("You have logged out! ");
			mRequestButton.setVisibility(View.INVISIBLE);
			mUploadButton.setVisibility(View.INVISIBLE);
			mPostButton.setVisibility(View.INVISIBLE);

			DataStorage.setLoggedIn(true);
			MapBrowserActivity.fb_button.setVisibility(4);

			// Save user profile to persistent storage
			SharedPreferences preferences = getSharedPreferences(Tools.PREFERENCE_FILE, MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = preferences.edit();
			prefsEditor.putBoolean("isLogin", false);
			Log.e("Log-out", "logout");
			prefsEditor.commit();
		}
	}

	public class SampleRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				final String name = json.getString("name");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				FacebookActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						mText.setText("Hello there, " + name + "!");
					}
				});
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	public class SampleUploadListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: (executed in background thread)
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				final String src = json.getString("src");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				FacebookActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						mText.setText("Hello there, photo has been uploaded at \n"
								+ src);
					}
				});
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	public class WallPostRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			Log.d("Facebook-Example", "Got response: " + response);
			String message = "<empty>";
			try {
				JSONObject json = Util.parseJson(response);
				message = json.getString("message");
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
			final String text = "Your Wall Post: " + message;
			FacebookActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					mText.setText(text);
				}
			});
		}
	}

	public class WallPostDeleteListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			if (response.equals("true")) {
				Log.d("Facebook-Example", "Successfully deleted wall post");
				FacebookActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						mDeleteButton.setVisibility(View.INVISIBLE);
						mText.setText("Deleted Wall Post");
					}
				});
			} else {
				Log.d("Facebook-Example", "Could not delete wall post");
			}
		}
	}

	public class SampleDialogListener extends BaseDialogListener {

		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");
			if (postId != null) {
				Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
				mAsyncRunner.request(postId, new WallPostRequestListener());
				mDeleteButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						mAsyncRunner.request(postId, new Bundle(), "DELETE",
								new WallPostDeleteListener(), null);
					}
				});
				mDeleteButton.setVisibility(View.VISIBLE);
			} else {
				Log.d("Facebook-Example", "No wall post made");
			}
		}

	}

	// ----------------
	public class WallPostListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {

		}
	}

	/**
	 * returns an list of recent posts by the user. each element in the list is
	 * an instance of class String
	 * 
	 * @return
	 * @throws FacebookError
	 * @throws JSONException
	 */
	public ArrayList getRecentPosts() throws FacebookError, JSONException {
		if (!mFacebook.isSessionValid())
			throw new FacebookError(
					"You haven't logged in or failed to log in.");

		openFbDb();

		ArrayList list = new ArrayList();
		JSONObject json = Util.parseJson(postsString);
		Log.d("json whole", json.toString());
		JSONArray arr = json.getJSONArray("data");
		int len = arr.length();
		for (int i = 0; i < len; i++) {
			try {
				JSONObject child = arr.getJSONObject(i);
				Log.d("child", child.toString());
				String post = child.getString("message");
				list.add(post);

				// Add to db
				long id = dbHelper.addPost(child.getString("id"),
						child.getString("message"));
			} catch (Exception e) {
				continue;
			}
		}
		DataStorage.setPosts(list);
		return list;
	}

	/**
	 * return a list of friends of the current user. Each element is the list is
	 * an instance of class FacebookProfile. first name and last name are not
	 * set for friends.
	 * 
	 * @return
	 * @throws FacebookError
	 * @throws JSONException
	 */
	public ArrayList getFriendsProfiles() throws FacebookError, JSONException {
		if (!mFacebook.isSessionValid())
			throw new FacebookError(
					"You haven't logged in or failed to log in.");
		openFbDb();

		ArrayList list = new ArrayList();
		JSONObject json = Util.parseJson(friendsString);
		JSONArray arr = json.getJSONArray("data");
		int len = arr.length();
		for (int i = 0; i < len; i++) {
			JSONObject child = arr.getJSONObject(i);
			FacebookProfile f = new FacebookProfile(child.getString("id"),
					child.getString("name"));
			list.add(f);

			// Add to db
			long id = dbHelper.addFriend(Long.valueOf(child.getString("id")),
					child.getString("name"));
		}
		DataStorage.setFriends(list);
		return list;
	}

	/**
	 * Get facebook user profile and save to DataStorage
	 * 
	 * @return
	 * @throws FacebookError
	 * @throws JSONException
	 */
	private FacebookProfile getUserProfile() throws FacebookError,
			JSONException {
		if (!mFacebook.isSessionValid())
			throw new FacebookError(
					"You haven't logged in or failed to log in.");
		JSONObject json = Util.parseJson(meString);
		FacebookProfile profile = new FacebookProfile(json.getString("id"),
				json.getString("name"));
		profile.setFirstName(json.getString("first_name"));
		profile.setLastName(json.getString("last_name"));
		try {
			String email = json.getString("email");
			profile.setEmail(email);
		} catch (Exception e) {
			Log.d("Email permission",
					"Email permission has not been set. Email address is not accessable.");
		}
		DataStorage.setMe(profile);
		return profile;
	}

	public void postToWall(String newPost) throws FacebookError, Exception {
		if (!mFacebook.isSessionValid())
			throw new FacebookError(
					"You haven't logged in or failed to log in.");
		if ((newPost == null) || (newPost.equals("")))
			return;
		Bundle params = new Bundle();
		params.putString("method", "stream.publish");
		params.putString("message", newPost);
		mAsyncRunner
				.request(null, params, "POST", new WallPostListener(), null);
	}

	private PostListener postListener = new PostListener() {
		@Override
		public void doPost(String message) {
			try {
				postToWall(message);
			} catch (FacebookError e) {
				Log.e("post error", e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("post error", e.getMessage());
				e.printStackTrace();
			}
		}

	};

	private void openFbDb() {
		// Create fb database
		if (dbHelper == null) {
			dbHelper = new DbAdapter(this);
			dbHelper.open();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

}
