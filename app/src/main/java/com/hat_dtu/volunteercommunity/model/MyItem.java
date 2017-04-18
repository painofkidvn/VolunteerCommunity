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

    public MyItem(double lat, double lng, String title, String address, String phone, String activity) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.activity = activity;
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

    @Override
    public String getSnippet() {
        return this.address;
    }
}
