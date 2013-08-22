package com.example.nearbyrestaurants.model;

public class Restaurant {

	private Coordinates coordinates;

	private String name;

	public Restaurant(String name) {
		super();
		this.name = name;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public String getName() {
		return name;
	}

	public String getDistance() {
		// TODO getDistance
		return null;
	}
}
