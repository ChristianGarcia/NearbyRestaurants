package com.example.nearbyrestaurants.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.example.nearbyrestaurants.model.Coordinates;

public class LastLocationProvider {

	public Coordinates getLastKnownLocation(Context context) {
		// Get the location manager
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		location.getLatitude();

		return new Coordinates(location.getLatitude(), location.getLongitude());

	}
}
