package com.hat_dtu.volunteercommunity.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.activity.CharityActivity;
import com.hat_dtu.volunteercommunity.activity.MainActivity;
import com.hat_dtu.volunteercommunity.adapter.CharityAdapter;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.app.AppController;
import com.hat_dtu.volunteercommunity.model.Charity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paino on 4/2/2017.
 */

public class CharityFragment extends Fragment {
    private static final String TAG = CharityFragment.class.getSimpleName();
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    CharityAdapter charityAdapter;
    ArrayList<Charity> charities = new ArrayList<>();
    public CharityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_charity, container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.rv_recycler_view);
        recyclerView.setHasFixedSize(true);

        charityAdapter = new CharityAdapter(charities);
        recyclerView.setAdapter(charityAdapter);
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

    private void onLoading(){
        String tag_string_req = "req_get_all_charity";
        progressDialog.setMessage("Loading ...");
        showDialog();
        Log.d(TAG, AppConfig.API_KEY);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_CHARITY_F, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response.substring(3));
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("charities");
                        for(int i = 0; i < jsonArray.length(); i++){

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String id = jsonObject.getString("id");
                            String title = jsonObject.getString("title");
                            String address = jsonObject.getString("address");
                            String phone = jsonObject.getString("phone");
                            String activity = jsonObject.getString("activity");
                            String rating = jsonObject.getString("rating");
                            String lat = jsonObject.getString("lat");
                            String lng = jsonObject.getString("lng");

                            charities.add(new Charity(id, title, address, phone, activity, rating, lat, lng));
                        }

                        Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                        error.getMessage(), Toast.LENGTH_LONG).show();
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
}
