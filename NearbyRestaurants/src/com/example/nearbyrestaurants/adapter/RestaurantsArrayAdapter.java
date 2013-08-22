package com.example.nearbyrestaurants.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nearbyrestaurants.R;
import com.example.nearbyrestaurants.model.Restaurant;

public class RestaurantsArrayAdapter extends ArrayAdapter<Restaurant> {

	public RestaurantsArrayAdapter(Context context, List<Restaurant> initialList) {
		super(context, R.layout.list_item_restaurant, initialList);
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
			viewHolder.tvDistance.setText(restaurant.getDistance() + " miles");
		}
		return convertView;
	}

	private static class RestaurantsViewHolder {
		TextView tvName;

		TextView tvDistance;
	}

}
