package com.example.nearbyrestaurants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nearbyrestaurants.R;
import com.example.nearbyrestaurants.mock.MockValues;
import com.example.nearbyrestaurants.model.Coordinates;
import com.example.nearbyrestaurants.model.Restaurant;

public class RestaurantsArrayAdapter extends ArrayAdapter<Restaurant> {

	private Coordinates centralPoint;

	public RestaurantsArrayAdapter(Context context) {
		super(context, R.layout.list_item_restaurant);
	}

	public Coordinates refreshCentralPoint() {
		// TODO refreshCentralPoint
		centralPoint = new Coordinates(MockValues.LAT, MockValues.LON);
		return centralPoint;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RestaurantsViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.list_item_restaurant, parent, false);

			viewHolder = new RestaurantsViewHolder();
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (RestaurantsViewHolder) convertView.getTag();
		}

		Restaurant restaurant = getItem(position);
		if (restaurant != null) {
			viewHolder.tvName.setText(restaurant.getName());
			viewHolder.tvDistance.setText(restaurant.getMilesFrom(centralPoint) + " miles");
		}
		return convertView;
	}

	private static class RestaurantsViewHolder {
		TextView tvName;

		TextView tvDistance;
	}

}
