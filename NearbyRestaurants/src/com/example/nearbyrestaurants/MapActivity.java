package com.example.nearbyrestaurants;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nearbyrestaurants.mock.MockValues;
import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Distance;
import com.example.nearbyrestaurants.model.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends RestaurantSearcherActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		centerInMyPosition();
	}

	private void centerInMyPosition() {
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		Coordinates userCoords = readUserPosition();

		LatLng userPosition = new LatLng(userCoords.getLatitude(), userCoords.getLongitude());

		addUserMarker(userPosition);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 10));

		map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

	}

	private void addUserMarker(LatLng userPosition) {
		MarkerOptions userPositionMarker = new MarkerOptions().position(userPosition)
																.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_indicator_current_position));
		map.addMarker(userPositionMarker);
	}

	private Coordinates readUserPosition() {
		// TODO readCentralPoint
		return new Coordinates(MockValues.LAT, MockValues.LON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean processed = super.onOptionsItemSelected(item);
		if (!processed) {
			if (item.getItemId() == R.id.action_list) {
				startActivity(new Intent(this, RestaurantListActivity.class));
			}
		}
		return false;
	}

	@Override
	public void searchRestaurantsFailed(List<Restaurant> result) {
		updateRestaurantsInfo(getDatabaseRestaurants());

	}

	@Override
	void updateRestaurantsInfo(List<Restaurant> foundRestaurants) {
		for (Restaurant restaurant : foundRestaurants) {
			addRestaurantMarker(restaurant);
		}
	}

	private void addRestaurantMarker(Restaurant restaurant) {
		Coordinates coordinates = restaurant.getCoordinates();
		double latitude = coordinates.getLatitude();
		double longitude = coordinates.getLongitude();
		LatLng position = new LatLng(latitude, longitude);
		Distance distance = restaurant.getDistanceFrom(readUserPosition());
		String distanceText = distance.getMiles() + " miles";

		MarkerOptions marker = new MarkerOptions().position(position)
													.title(restaurant.getName())
													.snippet(distanceText);
		map.addMarker(marker);
	}

}
