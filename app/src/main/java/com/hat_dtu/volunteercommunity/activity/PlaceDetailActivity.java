package com.hat_dtu.volunteercommunity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.app.AppController;
import com.hat_dtu.volunteercommunity.fragment.MyPlaceFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceDetailActivity extends AppCompatActivity {
    private static final String TAG = PlaceDetailActivity.class.getSimpleName();
    private EditText etTitle, etAddress, etPhone, etActivity;
    private Button btnCharityRes;
    private TextInputLayout inputLayoutTitle, inputLayoutAddress, inputLayoutPhone, inputLayoutActivity;
    private String title, address, phone, activity, lat, lng;
    private int id, user_id;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        etTitle = (EditText)findViewById(R.id.et_title_detail);
        etAddress = (EditText)findViewById(R.id.et_address_detail);
        etPhone = (EditText)findViewById(R.id.et_phone_detail);
        etActivity = (EditText)findViewById(R.id.et_activity_detail);
        btnCharityRes = (Button)findViewById(R.id.btn_register_charity_detail);
        inputLayoutTitle = (TextInputLayout)findViewById(R.id.input_layout_title_detail);
        inputLayoutAddress = (TextInputLayout)findViewById(R.id.input_layout_address_detail);
        inputLayoutPhone = (TextInputLayout)findViewById(R.id.input_layout_phone_detail);
        inputLayoutActivity = (TextInputLayout)findViewById(R.id.input_layout_activity_detail);


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Bundle bundle = getIntent().getBundleExtra("PLACE_DETAIL");
        etTitle.setText(bundle.getString("title"));
        etAddress.setText(bundle.getString("address"));
        etPhone.setText(bundle.getString("phone"));
        etActivity.setText(bundle.getString("activity"));
        id = bundle.getInt("id");
        user_id = bundle.getInt("user_id");


        btnCharityRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etTitle.getText().toString();
                address = etAddress.getText().toString();
                phone = etPhone.getText().toString();
                activity = etActivity.getText().toString();
                getLocation(address);
                onUpdate(id);
            }
        });
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();

    }
    private void onUpdate(int id) {

        String tag_string_req = "req_update_place";

        progressDialog.setMessage("Updating ...");
        showDialog();
        AppConfig.URL_PLACE_L = AppConfig.URL_PLACE_L+id;
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                AppConfig.URL_PLACE_L, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();
                try {
                    Log.d(TAG, response);
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Place successfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlaceDetailActivity.this, MainActivity.class);
                        AppConfig.isMyPlace = true;
                        startActivity(intent);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg.trim() == "" ? "Connection error": errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Updating Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("address", address);
                params.put("phone", phone);
                params.put("activity", activity);
                params.put("lat", lat);
                params.put("lng", lng);
                return params;
            }

        };
        AppConfig.URL_PLACE_L = "http://slimapp.esy.es/slimapp/places/";
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void getLocation(String add){
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(add,5);

            //check for null
            if (address == null) {
                return;
            }

            //Lets take first possibility from the all possibilities.
            Address location=address.get(0);
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
