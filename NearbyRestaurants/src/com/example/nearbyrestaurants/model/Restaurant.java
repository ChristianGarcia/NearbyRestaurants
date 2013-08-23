package com.example.nearbyrestaurants.model;

public class Restaurant {

	//TODO Refactor
	private static final double METERS_IN_ONE_MILE = 1609.344;

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

	public double getMilesFrom(Coordinates point) {
		return this.coordinates.metersTo(point) / METERS_IN_ONE_MILE;
	}
}
