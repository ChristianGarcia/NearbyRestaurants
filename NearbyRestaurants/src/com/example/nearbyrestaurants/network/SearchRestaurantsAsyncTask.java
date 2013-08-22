package com.example.nearbyrestaurants.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.nearbyrestaurants.model.Restaurant;

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

		String radiusInMeters = String.valueOf(distancesInMeters[0]);

		GooglePlacesNearbyRequest request = new GooglePlacesNearbyRequest("41.42,2.16", radiusInMeters, true, "restaurant");
		try {
			GooglePlacesResponse places = searchPlaces(request);
			List<Restaurant> restaurants = placesToRestaurants(places);
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
		return GooglePlacesResponse.fromResponseString(responseString);
	}

	private List<Restaurant> placesToRestaurants(GooglePlacesResponse places) {
		// TODO Auto-generated method stub
		return null;
	}

	private void addResultsInDatabase(List<Restaurant> result) {
		// TODO Auto-generated method stub

	}

}
