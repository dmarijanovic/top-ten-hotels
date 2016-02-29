package com.damirm.toptenhotels.models;

import com.google.android.gms.location.places.Place;

import java.io.Serializable;

public class PlaceData implements Serializable {

    public String placeId;
    public String name;
    public String street;
    public String pb;
    public String city;
    public String description;
    public float rating;

    public PlaceData(Place place) {
        this.name = place.getName().toString();
        String[] address = place.getAddress().toString().split(",");
        this.street = address.length >= 1 ? address[0] : "";
        this.pb = address.length >= 2 ? address[1] : "";
        this.city = address.length >= 3 ? address[2] : "";
        this.placeId = place.getId();
        this.description = "";
        this.rating = place.getRating();
    }

    public String getCityWithPb() {
        return pb + " " + city;
    }
}
