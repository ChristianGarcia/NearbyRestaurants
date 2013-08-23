package com.example.nearbyrestaurants.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nearbyrestaurants.googleplaces.GooglePlace;
import com.example.nearbyrestaurants.googleplaces.GooglePlacesNearbyRequest;
import com.example.nearbyrestaurants.googleplaces.GooglePlacesResponse;
import com.example.nearbyrestaurants.googleplaces.GooglePlacesRestaurantsNearbyRequest;
import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Restaurant;
import com.google.gson.Gson;

//TODO Wrap Double in distance to avoid conversion problems
public class SearchRestaurantsAsyncTask extends AsyncTask<Double, Void, List<Restaurant>> {

	private Activity launcherActivity;

	public SearchRestaurantsAsyncTask(Activity launcherActivity) {
		super();
		this.launcherActivity = launcherActivity;
	}

	public void setLauncherActivity(Activity launcherActivity) {
		this.launcherActivity = launcherActivity;
	}

	@Override
	protected List<Restaurant> doInBackground(Double... distancesInMeters) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		String radiusInMeters = String.valueOf(distancesInMeters[0]);

		GooglePlacesNearbyRequest request = new GooglePlacesRestaurantsNearbyRequest("41.42,2.16", radiusInMeters);
		try {
			GooglePlacesResponse places;
			String nextPageToken = "";
			do {
				request.setPageToken(nextPageToken);
				places = searchPlaces(request);
				nextPageToken = places.getNext_page_token();
				restaurants.addAll(placesToRestaurants(places));
			} while (nextPageToken != null);
			return restaurants;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(List<Restaurant> result) {
		addResultsInDatabase(result);
		if (launcherActivity != null && launcherActivity instanceof RestaurantSearcher) {
			RestaurantSearcher searcher = (RestaurantSearcher) launcherActivity;
			if (result != null) {
				searcher.searchRestaurantsSuccessful(result);
			} else {
				searcher.searchRestaurantsFailed(result);
			}
		}
	}

	private GooglePlacesResponse searchPlaces(GooglePlacesNearbyRequest request) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		String uri = request.getRequestURLString();
		Log.d("NR:Network", String.format("Requesting places URI: %s", uri));
		response = httpclient.execute(new HttpGet(uri));
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			responseString = out.toString();
		} else {
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
		GooglePlacesResponse gpResponse = googlePlacesResponseFromJSON(responseString);
		return gpResponse;
	}

	private GooglePlacesResponse googlePlacesResponseFromJSON(String responseString) {
		Gson gson = new Gson();
		GooglePlacesResponse gpResponse = gson.fromJson(responseString, GooglePlacesResponse.class);
		return gpResponse;
	}

	private List<Restaurant> placesToRestaurants(GooglePlacesResponse places) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		List<GooglePlace> results = places.getResults();
		for (GooglePlace result : results) {
			Restaurant restaurant = restaurantFromGooglePlace(result);
			restaurants.add(restaurant);
		}
		return restaurants;
	}

	private Restaurant restaurantFromGooglePlace(GooglePlace result) {
		String id = result.getId();
		String name = result.getName();

		double latitude = result.getGeometry().getLocation().getLat();
		double longitude = result.getGeometry().getLocation().getLng();
		Coordinates coordinates = new Coordinates(latitude, longitude);

		Restaurant restaurant = new Restaurant(id, name, coordinates);
		return restaurant;
	}

	private void addResultsInDatabase(List<Restaurant> result) {
		// TODO addResultsInDatabase

	}

}
