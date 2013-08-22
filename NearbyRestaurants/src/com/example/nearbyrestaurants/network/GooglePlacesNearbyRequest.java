package com.example.nearbyrestaurants.network;

import java.net.MalformedURLException;

public class GooglePlacesNearbyRequest {

	private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

	public static final String API_KEY_GOOGLE_PLACES = "AIzaSyBFuRSw_eDoSAolt4-5gyMMVpbjjgmg5Fg";

	private String location;

	private String radius;

	private boolean sensor;

	private String keyword;

	public GooglePlacesNearbyRequest(String location, String radius, boolean sensor, String keyword) {
		super();
		this.location = location;
		this.radius = radius;
		this.sensor = sensor;
		this.keyword = keyword;
	}

	public String getRequestURLString() throws MalformedURLException {
		StringBuffer sb = new StringBuffer(BASE_URL);
		sb.append("?key=" + API_KEY_GOOGLE_PLACES);
		sb.append("&location=" + location);
		sb.append("&radius=" + radius);
		sb.append("&sensor=" + sensor);
		sb.append("&keyword=" + keyword);
		return sb.toString();
	}

}
