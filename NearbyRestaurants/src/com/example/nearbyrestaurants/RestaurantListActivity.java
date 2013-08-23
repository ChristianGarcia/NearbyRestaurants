package com.example.nearbyrestaurants;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.nearbyrestaurants.adapter.RestaurantsArrayAdapter;
import com.example.nearbyrestaurants.comparator.DistanceToPointComparator;
import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Restaurant;
import com.example.nearbyrestaurants.task.RestaurantSearcher;
import com.example.nearbyrestaurants.task.SearchRestaurantsAsyncTask;

public class RestaurantListActivity extends Activity implements RestaurantSearcher {

	private static final double RADIUS_IN_MILES = 1.0;

	// TODO Refactor
	private static final double METERS_IN_ONE_MILE = 1609.344;

	private SearchRestaurantsAsyncTask searchRestaurantsTask;

	private RestaurantsArrayAdapter restaurantsArrayAdapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);

		configureListView();

		searchRestaurantsTask = (SearchRestaurantsAsyncTask) getLastNonConfigurationInstance();
		if (searchRestaurantsTask != null) {
			searchRestaurantsTask.setLauncherActivity(this);
		}
		showMessageForRestaurantsSearchedSoFar();
		fillListWithSavedRestaurants();

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return searchRestaurantsTask;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (searchRestaurantsTask != null) {
			searchRestaurantsTask.setLauncherActivity(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.restaurant_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_search) {
			searchRestaurantsInOneMile();
			return true;
		}
		return false;
	}

	private void searchRestaurantsInOneMile() {
		searchRestaurantsInRadius(RADIUS_IN_MILES * METERS_IN_ONE_MILE);
	}

	private void configureListView() {
		ListView lvRestaurant = (ListView) findViewById(R.id.listView);
		restaurantsArrayAdapter = new RestaurantsArrayAdapter(this);
		lvRestaurant.setAdapter(restaurantsArrayAdapter);
		lvRestaurant.setEmptyView(findViewById(R.id.empty));
	}

	private void fillListWithSavedRestaurants() {
		restaurantsArrayAdapter.clear();
		restaurantsArrayAdapter.addAll(getCachedRestaurants());
	}

	private List<Restaurant> getCachedRestaurants() {
		// TODO getCachedRestaurants
		return new ArrayList<Restaurant>();
	}

	@Override
	public void searchRestaurantsInRadius(Double distanceInMeters) {
		searchRestaurantsTask = new SearchRestaurantsAsyncTask(this);
		searchRestaurantsTask.execute(distanceInMeters);

	}

	@Override
	public void searchRestaurantsSuccessful(List<Restaurant> foundRestaurants) {
		if (restaurantsArrayAdapter != null) {
			restaurantsArrayAdapter.clear();
			restaurantsArrayAdapter.addAll(foundRestaurants);
			Coordinates centralPoint = restaurantsArrayAdapter.refreshCentralPoint();
			restaurantsArrayAdapter.sort(new DistanceToPointComparator(centralPoint));
			restaurantsArrayAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void searchRestaurantsFailed(List<Restaurant> result) {
		showOfflineModeMessage();
		fillListWithSavedRestaurants();
	}

	private void showOfflineModeMessage() {
		// TODO showOfflineModeMessage

	}

	private void showMessageForRestaurantsSearchedSoFar() {
		// TODO showMessageForRestaurantsSearchedSoFar if( adapter not empty)

	}

}
