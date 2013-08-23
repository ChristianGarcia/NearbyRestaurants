package com.example.nearbyrestaurants.model;

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

	public double getMiles() {
		return meters / DistanceMagnitude.MILE.inMeters;
	}

}
