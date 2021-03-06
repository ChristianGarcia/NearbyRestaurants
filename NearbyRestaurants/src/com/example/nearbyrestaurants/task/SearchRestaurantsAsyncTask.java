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

import com.example.nearbyrestaurants.database.RestaurantDataSource;
import com.example.nearbyrestaurants.googleplaces.GooglePlace;
import com.example.nearbyrestaurants.googleplaces.GooglePlacesNearbyRequest;
import com.example.nearbyrestaurants.googleplaces.GooglePlacesResponse;
import com.example.nearbyrestaurants.googleplaces.GooglePlacesRestaurantsNearbyRequest;
import com.example.nearbyrestaurants.location.LastLocationProvider;
import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Distance;
import com.example.nearbyrestaurants.model.Restaurant;
import com.google.gson.Gson;

public class SearchRestaurantsAsyncTask extends AsyncTask<Distance, Void, List<Restaurant>> {

	private Activity launcherActivity;

	public SearchRestaurantsAsyncTask(Activity launcherActivity) {
		super();
		this.launcherActivity = launcherActivity;
	}

	public void setLauncherActivity(Activity launcherActivity) {
		this.launcherActivity = launcherActivity;
	}

	@Override
	protected List<Restaurant> doInBackground(Distance... distances) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		double radiusInMeters = distances[0].getMeters();
		Coordinates userLocation = readCentralPoint();
		double latitude = userLocation.getLatitude();
		double longitude = userLocation.getLongitude();
		String location = latitude + "," + longitude;
		GooglePlacesNearbyRequest request = new GooglePlacesRestaurantsNearbyRequest(location,
				String.valueOf((int) radiusInMeters));
		try {
			GooglePlacesResponse places;
			String nextPageToken = "";
			do {
				request.setPageToken(nextPageToken);
				places = searchPlaces(request);
				nextPageToken = places.getNext_page_token();
				List<Restaurant> pageRestaurants = placesToRestaurants(places);
				pageRestaurants = discardFurtherThan(pageRestaurants, radiusInMeters);
				restaurants.addAll(pageRestaurants);
			} while (nextPageToken != null);
			return restaurants;
		} catch (IOException e) {
			return null;
		}
	}

	private List<Restaurant> discardFurtherThan(List<Restaurant> pageRestaurants, double radiusInMeters) {
		List<Restaurant> pageRestaurantsInRadius = new ArrayList<Restaurant>();
		Coordinates centralPoint = readCentralPoint();
		for (Restaurant pageRestaurant : pageRestaurants) {
			if (pageRestaurant.getCoordinates()
								.metersTo(centralPoint) < radiusInMeters) {
				pageRestaurantsInRadius.add(pageRestaurant);
			}
		}
		return pageRestaurantsInRadius;

	}

	private Coordinates readCentralPoint() {
		return new LastLocationProvider().getLastKnownLocation(launcherActivity);
	}

	@Override
	protected void onPostExecute(List<Restaurant> result) {
		if (launcherActivity != null && launcherActivity instanceof RestaurantSearcher) {
			RestaurantSearcher searcher = (RestaurantSearcher) launcherActivity;
			if (result != null) {
				addResultsInDatabase(result);
				searcher.searchRestaurantsSuccessful(result);
			} else {
				searcher.searchRestaurantsFailed(result);
			}
		}
	}

	private void addResultsInDatabase(List<Restaurant> result) {
		RestaurantDataSource datasource = new RestaurantDataSource(launcherActivity);
		datasource.open();
		for (Restaurant restaurant : result) {
			datasource.createRestaurant(restaurant);
		}
		datasource.close();
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
			response.getEntity()
					.writeTo(out);
			out.close();
			responseString = out.toString();
		} else {
			response.getEntity()
					.getContent()
					.close();
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

		double latitude = result.getGeometry()
								.getLocation()
								.getLat();
		double longitude = result.getGeometry()
									.getLocation()
									.getLng();
		Coordinates coordinates = new Coordinates(latitude, longitude);

		Restaurant restaurant = new Restaurant(id, name, coordinates);
		return restaurant;
	}

}
