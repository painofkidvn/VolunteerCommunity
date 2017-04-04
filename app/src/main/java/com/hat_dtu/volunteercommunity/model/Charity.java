package com.hat_dtu.volunteercommunity.model;

/**
 * Created by paino on 4/2/2017.
 */

public class Charity {
    private String id, title, address, phone, activity, rating, lat, lng;

    public Charity(){

    }

    public Charity(String id, String title, String address, String phone, String activity, String rating, String lat, String lng) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.activity = activity;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
