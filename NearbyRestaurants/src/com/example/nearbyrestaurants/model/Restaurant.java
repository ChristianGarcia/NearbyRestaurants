package com.example.nearbyrestaurants.model;

import com.example.nearbyrestaurants.model.Distance.DistanceMagnitude;

public class Restaurant {

	private String id;

	private String name;

	private Coordinates coordinates;

	public Restaurant(String id, String name, Coordinates coordinates) {
		super();
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public Distance getDistanceFrom(Coordinates point) {
		double distanceInMeters = this.coordinates.metersTo(point);
		Distance distance = new Distance(distanceInMeters, DistanceMagnitude.METER);
		return distance;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Restaurant) {
			return this.id.equals(((Restaurant) o).id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
