package com.hat_dtu.volunteercommunity.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.adapter.MyPlaceAdapter;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.app.AppController;
import com.hat_dtu.volunteercommunity.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paino on 4/2/2017.
 */

public class MyPlaceFragment extends Fragment {
    private static final String TAG = MyPlaceFragment.class.getSimpleName();
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    MyPlaceAdapter placeAdapter;
    String result = "";
    ArrayList<Place> places = new ArrayList<>();
    public MyPlaceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        onLoading();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_place, container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.rv_recycler_view);
        recyclerView.setHasFixedSize(true);
        placeAdapter = new MyPlaceAdapter(places, getContext());
        recyclerView.setAdapter(placeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        return view;
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();

    }
    public void onLoading(){
        String tag_string_req = "req_get_all_place";
        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_PLACE_F, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        places.clear();
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

                            places.add(new Place(id, title, address, phone, activity, lat, lng, user_id));
                            setUpJoinedValue(id);
                        }
                        /*for(int i = 0; i < places.size(); i++){
                            setUpJoinedValue(places.get(i).getId());

                        }
                        //placeAdapter.notifyDataSetChanged();*/

                        placeAdapter.notifyDataSetChanged();


                        Toast.makeText(getContext(), "Loaded", Toast.LENGTH_SHORT).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getContext(),
                                errorMsg.trim() == "" ? "Connection error": errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Creating Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                headers.put("Authorization", AppConfig.API_KEY);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void setUpJoinedValue(final int id){

        String tag_string_req = "req_load_joined";
        AppConfig.URL_JOIN =  AppConfig.URL_JOIN + "/" + id;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_JOIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        result =  jObj.getString("joined");
                        for(int i = 0; i < places.size(); i++){
                            if (places.get(i).getId() == id)
                                places.get(i).setJoined("Joined: " + result);
                        }
                        placeAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "Error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Creating Error: " + error.getMessage());

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
        Log.e("Xasdsa", ""+result);
        AppConfig.URL_JOIN = "http://slimapp.esy.es/slimapp/joined";
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
