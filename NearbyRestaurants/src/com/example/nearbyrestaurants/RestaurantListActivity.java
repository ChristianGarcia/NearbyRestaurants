package com.example.nearbyrestaurants;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant_list, menu);
		return true;
	}

}
