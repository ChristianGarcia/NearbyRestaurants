package com.example.nearbyrestaurants.network;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.nearbyrestaurants.model.Restaurant;

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
	protected List<Restaurant> doInBackground(Double... arg0) {
		// TODO Auto-generated method stub
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		restaurants.add(new Restaurant("Res 1"));
		return restaurants;
	}

	@Override
	protected void onPostExecute(List<Restaurant> result) {
		addResultsInDatabase(result);
		if (launcherActivity != null && launcherActivity instanceof RestaurantSearcher) {
			RestaurantSearcher searcher = (RestaurantSearcher) launcherActivity;
			searcher.updateRestaurantsInformation(result);
		}
	}

	private void addResultsInDatabase(List<Restaurant> result) {
		// TODO Auto-generated method stub

	}

}
