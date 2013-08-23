package com.example.nearbyrestaurants;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nearbyrestaurants.adapter.RestaurantsArrayAdapter;
import com.example.nearbyrestaurants.database.RestaurantDataSource;
import com.example.nearbyrestaurants.model.Distance;
import com.example.nearbyrestaurants.model.Distance.DistanceMagnitude;
import com.example.nearbyrestaurants.model.Restaurant;
import com.example.nearbyrestaurants.task.RestaurantSearcher;
import com.example.nearbyrestaurants.task.SearchRestaurantsAsyncTask;

public class RestaurantListActivity extends Activity implements RestaurantSearcher {

	private RestaurantDataSource datasource;

	private static final double RADIUS_IN_MILES = 1.0;

	private TextView tvMessage;

	private SearchRestaurantsAsyncTask searchRestaurantsTask;

	private RestaurantsArrayAdapter restaurantsArrayAdapter;

	private TextView emptyView;

	private View tvOffline;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);
		tvMessage = (TextView) findViewById(R.id.message);
		tvOffline = findViewById(R.id.offline_message);
		configureListView();

		searchRestaurantsTask = (SearchRestaurantsAsyncTask) getLastNonConfigurationInstance();
		if (searchRestaurantsTask != null) {
			searchRestaurantsTask.setLauncherActivity(this);
		}

		datasource = new RestaurantDataSource(this);
		datasource.open();

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
		Distance distance = new Distance(RADIUS_IN_MILES, DistanceMagnitude.MILE);
		searchRestaurantsInRadius(distance);
	}

	private void configureListView() {
		ListView lvRestaurant = (ListView) findViewById(R.id.listView);
		restaurantsArrayAdapter = new RestaurantsArrayAdapter(this);
		lvRestaurant.setAdapter(restaurantsArrayAdapter);
		emptyView = (TextView) findViewById(R.id.empty);
		lvRestaurant.setEmptyView(emptyView);
	}

	private void fillListWithSavedRestaurants() {
		List<Restaurant> savedRestaurants = getSavedRestaurants();
		restaurantsArrayAdapter.setNewRestaurantList(savedRestaurants);
		showMessageForSavedRestaurants();
	}

	private List<Restaurant> getSavedRestaurants() {
		return datasource.getAllRestaurants();
	}

	@Override
	public void searchRestaurantsInRadius(Distance distance) {
		searchRestaurantsTask = new SearchRestaurantsAsyncTask(this);
		searchRestaurantsTask.execute(distance);

	}

	@Override
	public void searchRestaurantsSuccessful(List<Restaurant> foundRestaurants) {

		addResultsInDatabase(foundRestaurants);

		if (restaurantsArrayAdapter != null) {
			hideOfflineModeMessage();

			restaurantsArrayAdapter.setNewRestaurantList(foundRestaurants);

			if (foundRestaurants.isEmpty()) {
				emptyView.setText(R.string.no_restaurants_found);
			}
			tvMessage.setVisibility(View.GONE);
		}
	}

	@Override
	public void searchRestaurantsFailed(List<Restaurant> result) {
		showOfflineModeMessage();
		fillListWithSavedRestaurants();
	}

	private void showOfflineModeMessage() {
		tvOffline.setVisibility(View.VISIBLE);
	}

	private void hideOfflineModeMessage() {
		tvOffline.setVisibility(View.GONE);

	}

	private void showMessageForSavedRestaurants() {
		showMessage(R.string.message_saved_list);
	}

	private void showMessage(int messageId) {
		if (!restaurantsArrayAdapter.isEmpty()) {
			tvMessage.setText(messageId);
			tvMessage.setVisibility(View.VISIBLE);
		} else {
			tvMessage.setVisibility(View.GONE);
		}
	}

	private void addResultsInDatabase(List<Restaurant> result) {
		for (Restaurant restaurant : result) {
			datasource.createRestaurant(restaurant);
		}
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

}
