package com.example.nearbyrestaurants.comparator;

import java.util.Comparator;

import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Restaurant;

public class DistanceToPointComparator implements Comparator<Restaurant> {

	private Coordinates centralPoint;

	public DistanceToPointComparator(Coordinates centralPoint) {
		this.centralPoint = centralPoint;
	}

	@Override
	public int compare(Restaurant restaurant1, Restaurant restaurant2) {
		double distance1 = centralPoint.metersTo(restaurant1.getCoordinates());
		double distance2 = centralPoint.metersTo(restaurant2.getCoordinates());
		return (int) (distance1 - distance2);
	}

}
