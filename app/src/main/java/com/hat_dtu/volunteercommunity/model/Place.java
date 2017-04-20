package com.hat_dtu.volunteercommunity.model;

/**
 * Created by paino on 4/2/2017.
 */

public class Place {
    private int id, user_id;
    private String title, address, phone, activity, lat, lng, joined;
    private boolean mChecked;
    private boolean mActivateExpansion = false;

    public Place(){

    }

    public Place(int id, String title, String address, String phone, String activity, String lat, String lng, int user_id) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.activity = activity;
        this.lat = lat;
        this.lng = lng;
        this.user_id = user_id;
    }
    public Place(int id, String title, String address, String phone, String activity, String lat, String lng, int user_id, String joined) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.activity = activity;
        this.lat = lat;
        this.lng = lng;
        this.user_id = user_id;
        this.joined = joined;
    }

    public String getJoined() {
        return this.joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public boolean isActivateExpansion() {
        return mActivateExpansion;
    }

    public void setActivateExpansion(boolean activateExpansion) {
        mActivateExpansion = activateExpansion;
    }
}
