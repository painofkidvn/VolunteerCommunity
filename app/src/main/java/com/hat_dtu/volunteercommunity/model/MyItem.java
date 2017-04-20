package com.hat_dtu.volunteercommunity.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by paino on 4/4/2017.
 */

public class MyItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String address;
    private final String phone;
    private final String activity;
    private String joined;
    private int id;
    private boolean isOwner;

    public MyItem(double lat, double lng, String title, String address, String phone, String activity) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.activity = activity;
    }
    public MyItem(double lat, double lng, String title, String address, String phone, String activity, String joined) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.activity = activity;
        this.joined = joined;
    }

    @Override
    public LatLng getPosition() {
        return this.position;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
    public String getPhone() {
        return this.phone;
    }

    public String getActivity() {
        return this.activity;
    }

    public String getJoined() {
        return joined;
    }
    public void setJoined(String joined){this.joined = joined;}

    public int getId() {
        return id;
    }
    public boolean getIsOwner(){
        return this.isOwner;
    }
    public void setIsOwner(boolean isOwner){
        this.isOwner = isOwner;
    }


    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getSnippet() {
        return this.address;
    }
}
