package com.example.nearbyrestaurants.googleplaces;

import java.util.List;

public class GooglePlacesResponse {

	private String next_page_token;

	private List<GooglePlace> results;

	public GooglePlacesResponse() {

	}

	public String getNext_page_token() {
		return next_page_token;
	}

	public List<GooglePlace> getResults() {
		return results;
	}

}
