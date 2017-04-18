package com.hat_dtu.volunteercommunity.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.hat_dtu.volunteercommunity.activity.PlaceActivity;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.app.AppController;
import com.hat_dtu.volunteercommunity.helper.SQLiteHandler;
import com.hat_dtu.volunteercommunity.model.MyItem;
import com.hat_dtu.volunteercommunity.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paino on 2/28/2017.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private BottomSheetBehavior bottomSheetBehavior;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private ProgressDialog progressDialog;
    private FloatingActionButton fabAdd;
    private TextView tvTitle, tvAddress, tvPhone, tvActivity;
    private ArrayList<Place>  places = new ArrayList<>();
    private FrameLayout frameLayout;
    private SQLiteHandler db;
    private LatLng latLng;
    ClusterManager<MyItem> clusterManager;

    public HomeFragment() {
    }
    public HomeFragment(LatLng latLng){
        this.latLng = latLng;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        db = new SQLiteHandler(getContext());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tvTitle = (TextView)rootView.findViewById(R.id.tv_btm_title);
        tvActivity = (TextView)rootView.findViewById(R.id.tv_btm_activity);
        tvAddress = (TextView)rootView.findViewById(R.id.tv_btm_address);
        tvPhone = (TextView)rootView.findViewById(R.id.tv_btm_phone);
        fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add_charity);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                startActivity(intent);
            }
        });

        frameLayout = (FrameLayout)rootView.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        //recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_search_recycler_view);
        //searchAdapter = new SearchAdapter(getContext(), places);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_home);
        supportMapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Map Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        showDialog();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        clusterManager = new ClusterManager<>(getContext(), map);
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                onLoadingAll();
                if(AppConfig.isMove == true)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

            }
        });
        map.getUiSettings().setZoomGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 4 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //Permission don't granted
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getContext(), "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permission don't granted and dont show dialog again.
                else {
                    Toast.makeText(getContext(), "Permission don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            }
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    public void onLoadingAll(){
        String tag_string_req = "req_load_all_place";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_PLACE_A, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        places.clear();
                        MyItem item;
                        JSONArray jsonArray = jObj.getJSONArray("places");
                        for(int i = 0; i < jsonArray.length(); i++){

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            int id = jsonObject.getInt("id");
                            String title = jsonObject.getString("title");
                            String address = jsonObject.getString("address");
                            String phone = jsonObject.getString("phone");
                            String activity = jsonObject.getString("activity");
                            String lat = jsonObject.getString("lat");
                            String lng = jsonObject.getString("lng");
                            int user_id = jsonObject.getInt("user_id");
                            map.setOnCameraIdleListener(clusterManager);
                            map.setOnMarkerClickListener(clusterManager);
                            item = new MyItem(Double.parseDouble(lat), Double.parseDouble(lng), title, address, phone, activity);

                            places.add(new Place(id, title, address, phone, activity, lat, lng, user_id));
                            db.addPlace(title, address, phone, activity, lat, lng, String.valueOf(user_id));
                            clusterManager.addItem(item);

                            clusterManager.cluster();

                        }

                        Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_LONG).show();
                        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
                            @Override
                            public boolean onClusterClick(Cluster<MyItem> cluster) {

                                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    bottomSheetBehavior.setPeekHeight(120);
                                } else {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    bottomSheetBehavior.setPeekHeight(0);
                                }
                                return true;
                            }
                        });
                        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                            @Override
                            public boolean onClusterItemClick(MyItem myItem) {
                                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    tvTitle.setText(myItem.getTitle());
                                    tvPhone.setText(myItem.getPhone());
                                    tvAddress.setText(myItem.getSnippet());
                                    tvActivity.setText(myItem.getActivity());
                                    bottomSheetBehavior.setPeekHeight(120);
                                } else {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    bottomSheetBehavior.setPeekHeight(0);
                                }
                                return true;
                            }
                        });

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),
                                errorMsg.trim() == "" ? "Connection error": errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Creating Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                headers.put("Authorization", AppConfig.API_KEY);
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}

