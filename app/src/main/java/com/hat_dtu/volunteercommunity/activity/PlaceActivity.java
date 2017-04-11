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
import com.hat_dtu.volunteercommunity.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceActivity extends AppCompatActivity {
    private static final String TAG = PlaceActivity.class.getSimpleName();
    private EditText etTitle, etAddress, etPhone, etActivity;
    private Button btnCharityRes;
    private TextInputLayout inputLayoutTitle, inputLayoutAddress, inputLayoutPhone, inputLayoutActivity;
    private String title, address, phone, activity, rating = "0", lat, lng;
    private ProgressDialog progressDialog;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        etTitle = (EditText)findViewById(R.id.et_title);
        etAddress = (EditText)findViewById(R.id.et_address);
        etPhone = (EditText)findViewById(R.id.et_phone);
        etActivity = (EditText)findViewById(R.id.et_activity);
        btnCharityRes = (Button)findViewById(R.id.btn_register_charity);
        inputLayoutTitle = (TextInputLayout)findViewById(R.id.input_layout_title);
        inputLayoutAddress = (TextInputLayout)findViewById(R.id.input_layout_address);
        inputLayoutPhone = (TextInputLayout)findViewById(R.id.input_layout_phone);
        inputLayoutActivity = (TextInputLayout)findViewById(R.id.input_layout_activity);


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);



        btnCharityRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
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
    private void submitForm() {
        if(!validateTitle())
            return;
        if(!validateAddress())
            return;
        if(!validatePhone())
            return;
        if(!validateActivity())
            return;
        onCreateSuccess();
    }

    private void onCreateSuccess() {
// Tag used to cancel the request
        String tag_string_req = "req_create_charity";

        progressDialog.setMessage("Creating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PLACE_F, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();
                try {
                    Log.d(TAG, response);
                    JSONObject jObj = new JSONObject(response.substring(3));
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Place Location successfully created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlaceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Creating Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public  Map<String, String> getHeaders() {
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private boolean validateActivity() {
        activity = etActivity.getText().toString();
        if(activity.isEmpty()){
            inputLayoutActivity.setError(getString(R.string.err_msg_activity));
            requestFocus(etActivity);
            return false;
        }
        inputLayoutActivity.setErrorEnabled(false);
        return true;
    }

    private boolean validatePhone() {
        phone = etPhone.getText().toString();
        if(phone.isEmpty()){
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(etPhone);
            return false;
        }
        inputLayoutPhone.setErrorEnabled(false);
        return true;
    }

    private boolean validateAddress() {
        address = etAddress.getText().toString();
        if(address.isEmpty()){
            inputLayoutAddress.setError(getString(R.string.err_msg_address));
            requestFocus(etAddress);
            return false;
        }
        inputLayoutAddress.setErrorEnabled(false);
        getLocation(address);
        return true;
    }

    private boolean validateTitle() {
        title = etTitle.getText().toString();
        if(title.isEmpty()){
            inputLayoutTitle.setError(getString(R.string.err_msg_title));
            requestFocus(etTitle);
            return false;
        }
        inputLayoutTitle.setErrorEnabled(false);
        return true;
    }
    private void requestFocus(View view) {
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

