package com.damirm.toptenhotels.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.damirm.toptenhotels.R;
import com.damirm.toptenhotels.models.PlaceData;
import com.damirm.toptenhotels.utils.PhotoUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<PlaceData> placesData;
    private GoogleApiClient apiClient;

    public PlaceAdapter(Context context, GoogleApiClient apiClient) {
        this.inflater = LayoutInflater.from(context);
        this.placesData = new ArrayList<>();
        this.apiClient = apiClient;
    }

    public void setPlaces(List<PlaceData> PlaceData) {
        this.placesData.clear();
        this.placesData.addAll(PlaceData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return placesData.size();
    }

    @Override
    public Object getItem(int position) {
        return placesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlacesViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_tem, null);
            vh = new PlacesViewHolder(convertView);
        } else {
            vh = (PlacesViewHolder) convertView.getTag();
        }

        PlaceData placeData = (PlaceData) getItem(position);
        vh.name.setText(placeData.name);
        vh.address.setText(placeData.street);
        vh.city.setText(placeData.getCityWithPb());

        PhotoUtil.downloadOrGetFromCache(apiClient, vh.imageView, placeData.placeId, 0, true);

        return convertView;
    }

    private class PlacesViewHolder {
        public TextView name;
        public TextView address;
        public TextView city;
        public ImageView imageView;

        public PlacesViewHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.name);
            this.address = (TextView) view.findViewById(R.id.address);
            this.city = (TextView) view.findViewById(R.id.city);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);

            view.setTag(this);
        }
    }
}
