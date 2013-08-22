package com.example.nearbyrestaurants.task;

import java.util.List;

import com.example.nearbyrestaurants.model.Restaurant;

public interface RestaurantSearcher {

	void searchRestaurantsInRadius(Double distanceinMiles);

	void searchRestaurantsSuccessful(List<Restaurant> foundRestaurants);

	void searchRestaurantsFailed(List<Restaurant> result);

}
