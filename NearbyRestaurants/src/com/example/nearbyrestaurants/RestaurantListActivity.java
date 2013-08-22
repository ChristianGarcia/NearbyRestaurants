package com.example.nearbyrestaurants;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.nearbyrestaurants.adapter.RestaurantsArrayAdapter;
import com.example.nearbyrestaurants.model.Restaurant;

public class RestaurantListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);

		ListView lvRestaurant = (ListView) findViewById(R.id.listView);

		List<Restaurant> restaurants = getCachedRestaurants();
		lvRestaurant.setAdapter(new RestaurantsArrayAdapter(this, restaurants));
		lvRestaurant.setEmptyView(findViewById(R.id.empty));

	}

	private List<Restaurant> getCachedRestaurants() {
		// TODO getCachedRestaurants
		return new ArrayList<Restaurant>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.restaurant_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_search) {
			updateRestaurantsFromNetwork();
			return true;
		}
		return false;
	}

	private void updateRestaurantsFromNetwork() {
		// TODO Auto-generated method stub
		
	}

}
