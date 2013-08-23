package com.example.nearbyrestaurants.task;

import java.util.List;

import com.example.nearbyrestaurants.model.Distance;
import com.example.nearbyrestaurants.model.Restaurant;

public interface RestaurantSearcher {

	void searchRestaurantsSuccessful(List<Restaurant> foundRestaurants);

	void searchRestaurantsFailed(List<Restaurant> result);

	void searchRestaurantsInRadius(Distance distance);

}
