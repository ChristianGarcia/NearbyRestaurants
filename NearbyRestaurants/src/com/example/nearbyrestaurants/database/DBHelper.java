package com.example.nearbyrestaurants.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String TABLE_RESTAURANTS = "restaurants";

	public static final String COLUMN_ID = "id";

	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_LAT = "lat";

	public static final String COLUMN_LON = "lon";

	private static final String DATABASE_NAME = "restaurants.db";

	private static final int DATABASE_VERSION = 1;

	// @formatter:off
	private static final String DATABASE_CREATE = "create table " + 
			TABLE_RESTAURANTS + "("
				+ COLUMN_ID		+ " text primary key, "
				+ COLUMN_NAME	+ " text not null, "
				+ COLUMN_LAT	+ " text not null, "
				+ COLUMN_LON 	+ " text not null" +
			");";
	// @formatter:on

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
