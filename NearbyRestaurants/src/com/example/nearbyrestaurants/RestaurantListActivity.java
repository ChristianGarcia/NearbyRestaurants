package com.example.nearbyrestaurants;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nearbyrestaurants.adapter.RestaurantsArrayAdapter;
import com.example.nearbyrestaurants.model.Restaurant;

public class RestaurantListActivity extends RestaurantSearcherActivity {

	private TextView tvMessage;

	private View tvOffline;

	private RestaurantsArrayAdapter restaurantsArrayAdapter;

	private TextView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);
		tvMessage = (TextView) findViewById(R.id.message);
		tvOffline = findViewById(R.id.offline_message);
		configureListView();

		fillListWithSavedRestaurants();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.restaurant_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean processed = super.onOptionsItemSelected(item);
		if (!processed) {
			// TODO process open map
		}
		return false;
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

	@Override
	void updateRestaurantsInfo(List<Restaurant> foundRestaurants) {
		if (restaurantsArrayAdapter != null) {
			hideOfflineModeMessage();

			restaurantsArrayAdapter.setNewRestaurantList(foundRestaurants);

			if (foundRestaurants.isEmpty()) {
				emptyView.setText(R.string.no_restaurants_found);
			}
			tvMessage.setVisibility(View.GONE);
		}
	}

}
