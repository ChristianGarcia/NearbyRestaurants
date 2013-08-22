package com.example.nearbyrestaurants.network;

import java.util.List;

public class GooglePlacesResponse {

	private List<GooglePlace> places;

	public static GooglePlacesResponse fromResponseString(String responseString) {
		GooglePlacesResponse response = new GooglePlacesResponse();
		return response;
	}

	private GooglePlacesResponse() {

	}

	public List<GooglePlace> getPlaces() {
		return places;
	}

}
