package edu.usc.imsc.db;

/**
 * this class provides the functionality to query, create and update
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_MESSAGE = "message";
	private static final String DATABASE_TABLE_FRIENDS = "fb_friends";
	private static final String DATABASE_TABLE_POSTS = "fb_posts";
	
	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	public DbAdapter(Context context) {
		this.context = context;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * add a friends/posts table If the friends is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */

	public long addFriend(long id, String name) {
		ContentValues initialValues = createContentValuesForFriends(id, name);
		return database.insert(DATABASE_TABLE_FRIENDS, null, initialValues);
	}
	
	public long addPost(String id, String message) {
		ContentValues initialValues = createContentValuesForPosts(id, message);
		return database.insert(DATABASE_TABLE_POSTS, null, initialValues);
	}

	/**
	 * Update the friends/posts
	 */

	public boolean updateFriend(long rowId, String name) {
		ContentValues updateValues = createContentValuesForFriends(rowId, name);

		return database.update(DATABASE_TABLE_FRIENDS, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}
	
	public boolean updatePost(long rowId, String message) {
		ContentValues updateValues = createContentValuesForFriends(rowId, message);

		return database.update(DATABASE_TABLE_POSTS, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	/**
	 * Deletes friends/posts
	 */

	public boolean deleteFriend(long rowId) {
		return database.delete(DATABASE_TABLE_FRIENDS, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deletePost(long rowId) {
		return database.delete(DATABASE_TABLE_POSTS, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all friends/posts in the database
	 * 
	 * @return Cursor over all data
	 */

	public Cursor fetchAllFriends() {
		return database.query(DATABASE_TABLE_FRIENDS, new String[] { KEY_ROWID,
				KEY_NAME}, null, null, null, null, null);
	}
	
	public Cursor fetchAllPosts() {
		return database.query(DATABASE_TABLE_POSTS, new String[] { KEY_ROWID,
				KEY_MESSAGE}, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the defined friends/posts
	 */

	public Cursor fetchFriends(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE_FRIENDS, new String[] {
				KEY_ROWID, KEY_NAME },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetchPosts(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE_POSTS, new String[] {
				KEY_ROWID, KEY_MESSAGE },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValuesForFriends(long id, String name) {
		ContentValues values = new ContentValues();
		values.put(KEY_ROWID, String.valueOf(id));
		values.put(KEY_NAME, name);
		return values;
	}
	
	private ContentValues createContentValuesForPosts(String id, String message) {
		ContentValues values = new ContentValues();
		values.put(KEY_ROWID, id);
		values.put(KEY_MESSAGE, message);
		return values;
	}
}
