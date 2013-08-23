package com.example.nearbyrestaurants.model;

import java.util.Locale;

public class Distance {

	public Distance(double amount, DistanceMagnitude magnitude) {
		super();
		this.meters = amount * magnitude.inMeters;
	}

	private double meters;

	public static enum DistanceMagnitude {
		METER(1), MILE(1609.344), KILOMETER(1000);
		double inMeters;

		DistanceMagnitude(double inMeters) {
			this.inMeters = inMeters;
		}
	}

	public double getMeters() {
		return this.meters;
	}

	public String getMilesText() {
		double miles = meters / DistanceMagnitude.MILE.inMeters;
		return String.format(new Locale("en", "gb"), "%.2f miles", miles);
	}

}
