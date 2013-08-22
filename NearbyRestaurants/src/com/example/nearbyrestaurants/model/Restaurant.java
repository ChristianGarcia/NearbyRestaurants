package com.example.nearbyrestaurants.model;

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

	public String getDistance() {
		// TODO getDistance
		return null;
	}
}
