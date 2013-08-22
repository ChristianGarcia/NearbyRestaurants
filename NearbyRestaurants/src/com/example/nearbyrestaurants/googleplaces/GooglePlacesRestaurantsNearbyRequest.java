package com.example.nearbyrestaurants.googleplaces;

public class GooglePlacesRestaurantsNearbyRequest extends GooglePlacesNearbyRequest {

	private static final String KEYWORD = "restaurant";

	public GooglePlacesRestaurantsNearbyRequest(String location, String radius) {
		super(location, radius, true, KEYWORD);
	}

}
