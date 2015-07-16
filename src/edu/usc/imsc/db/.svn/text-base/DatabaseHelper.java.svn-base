package edu.usc.imsc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "fb_db";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String FRIENDS_TABLE_CREATE = "create table fb_friends (_id numeric primary key, "
			+ "name text not null);";
	private static final String POSTS_TABLE_CREATE = "create table fb_posts (_id text primary key, "
		+ "message text not null);";
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(FRIENDS_TABLE_CREATE);
		database.execSQL(POSTS_TABLE_CREATE);
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS todo");
		onCreate(database);
	}
}