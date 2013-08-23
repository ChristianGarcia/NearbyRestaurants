package com.example.nearbyrestaurants;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.nearbyrestaurants.database.RestaurantDataSource;
import com.example.nearbyrestaurants.model.Distance;
import com.example.nearbyrestaurants.model.Distance.DistanceMagnitude;
import com.example.nearbyrestaurants.model.Restaurant;
import com.example.nearbyrestaurants.task.RestaurantSearcher;
import com.example.nearbyrestaurants.task.SearchRestaurantsAsyncTask;

public abstract class RestaurantSearcherActivity extends Activity implements RestaurantSearcher {
	private static final double RADIUS_IN_MILES = 1.0;

	private RestaurantDataSource datasource;

	private SearchRestaurantsAsyncTask searchRestaurantsTask;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		searchRestaurantsTask = (SearchRestaurantsAsyncTask) getLastNonConfigurationInstance();
		if (searchRestaurantsTask != null) {
			searchRestaurantsTask.setLauncherActivity(this);
		}

		datasource = new RestaurantDataSource(this);
		datasource.open();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return searchRestaurantsTask;
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (searchRestaurantsTask != null) {
			searchRestaurantsTask.setLauncherActivity(null);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_search) {
			searchRestaurantsInOneMile();
			return true;
		}
		return false;
	}

	protected List<Restaurant> getSavedRestaurants() {
		return datasource.getAllRestaurants();
	}

	private void searchRestaurantsInOneMile() {
		Distance distance = new Distance(RADIUS_IN_MILES, DistanceMagnitude.MILE);
		searchRestaurantsInRadius(distance);
	}

	@Override
	public void searchRestaurantsSuccessful(List<Restaurant> foundRestaurants) {
		addResultsInDatabase(foundRestaurants);
		updateRestaurantsInfo(foundRestaurants);
	}

	abstract void updateRestaurantsInfo(List<Restaurant> foundRestaurants);

	@Override
	public void searchRestaurantsInRadius(Distance distance) {
		searchRestaurantsTask = new SearchRestaurantsAsyncTask(this);
		searchRestaurantsTask.execute(distance);
	}

	private void addResultsInDatabase(List<Restaurant> result) {
		for (Restaurant restaurant : result) {
			datasource.createRestaurant(restaurant);
		}
	}

}
