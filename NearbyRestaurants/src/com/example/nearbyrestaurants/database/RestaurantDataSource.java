package com.example.nearbyrestaurants.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nearbyrestaurants.database.exception.StoredCoordinatesException;
import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Restaurant;

public class RestaurantDataSource {
	// Database fields
	private SQLiteDatabase database;

	private DBHelper dbHelper;

	private String[] allColumns = { DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_LAT, DBHelper.COLUMN_LON };

	public RestaurantDataSource(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createRestaurant(Restaurant restaurant) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_ID, restaurant.getId());
		values.put(DBHelper.COLUMN_NAME, restaurant.getName());
		values.put(DBHelper.COLUMN_LAT, restaurant.getCoordinates().getLatitude());
		values.put(DBHelper.COLUMN_LON, restaurant.getCoordinates().getLongitude());
		database.insert(DBHelper.TABLE_RESTAURANTS, null, values);
	}
	
	public void deleteRestaurant(Restaurant restaurant) {
		String id = restaurant.getId();
		database.delete(DBHelper.TABLE_RESTAURANTS, DBHelper.COLUMN_ID + " = " + id, null);
	}

	public List<Restaurant> getAllRestaurants() {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();

		Cursor cursor = database.query(DBHelper.TABLE_RESTAURANTS, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			try {
				Restaurant restaurant = cursorToRestaurant(cursor);
				restaurants.add(restaurant);
			} catch (StoredCoordinatesException e) {
				Log.w("NR:RestaurantDataSource", "Error reading stored coordinates");
			}
			cursor.moveToNext();
		}
		cursor.close();
		return restaurants;
	}

	private Restaurant cursorToRestaurant(Cursor cursor) throws StoredCoordinatesException {
		String id = cursor.getString(0);
		String name = cursor.getString(1);
		try {
			double latitude = Double.parseDouble(cursor.getString(2));
			double longitude = Double.parseDouble(cursor.getString(3));
			Restaurant restaurant = new Restaurant(id, name, new Coordinates(latitude, longitude));
			return restaurant;
		} catch (NumberFormatException nfe) {
			throw new StoredCoordinatesException();
		}

	}
}
