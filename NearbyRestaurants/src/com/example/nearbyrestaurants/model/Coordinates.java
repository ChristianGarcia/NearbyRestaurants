package com.example.nearbyrestaurants.model;

public class Coordinates {

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

}
