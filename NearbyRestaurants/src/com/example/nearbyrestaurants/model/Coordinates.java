package com.example.nearbyrestaurants.model;

public class Coordinates {

	private static final int EARTH_RADIUS_IN_METERS = 6371000;

	private double latitude;

	private double longitude;

	public Coordinates(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double metersTo(Coordinates point) {
		double lat1 = this.latitude;
		double lon1 = this.longitude;
		double lat2 = point.latitude;
		double lon2 = point.longitude;
		
		double latDiffInRadians = deg2rad(lat2 - lat1);
		double lonDiffInRadians = deg2rad(lon2 - lon1);
		
		double a = Math.sin(latDiffInRadians / 2) * Math.sin(latDiffInRadians / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.sin(lonDiffInRadians / 2) * Math.sin(lonDiffInRadians / 2);
		
		double arcInRadians = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distanceInKm = EARTH_RADIUS_IN_METERS * arcInRadians;
		return distanceInKm;
	}

	private double deg2rad(double degrees) {
		return degrees * (Math.PI / 180);
	}

}
