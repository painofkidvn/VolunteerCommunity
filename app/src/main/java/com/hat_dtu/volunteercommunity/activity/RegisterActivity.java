package com.hat_dtu.volunteercommunity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.app.AppController;
import com.hat_dtu.volunteercommunity.helper.SQLiteHandler;
import com.hat_dtu.volunteercommunity.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText etFullname, etPassword, etCPassword, etEmail;
    private Button btnSignUp;
    private String fullname, password, cpassword, email;
    private TextInputLayout inputLayoutFullname, inputLayoutPassword, inputLayoutCPassword, inputLayoutEmail;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView tvSignInNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputLayoutFullname = (TextInputLayout) findViewById(R.id.input_layout_fullname);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutCPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        etFullname = (EditText) findViewById(R.id.et_fullname);
        etPassword = (EditText) findViewById(R.id.et_password);
        etCPassword = (EditText) findViewById(R.id.et_confirm_password);
        etEmail = (EditText) findViewById(R.id.et_email);

        etFullname.addTextChangedListener(new MyTextWatcher(etFullname));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        etCPassword.addTextChangedListener(new MyTextWatcher(etCPassword));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        tvSignInNow = (TextView)findViewById(R.id.tv_sign_in_now);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getBaseContext());

        // SQLite database handler
        db = new SQLiteHandler(getBaseContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        tvSignInNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void submitForm() {
        if(!validateName())
            return;
        if(!validatePassword())
            return;
        if(!validateCPassword())
            return;
        if(!validateEmail())
            return;
        onSignUpSuccess();
    }


    private boolean validateName() {
        fullname = etFullname.getText().toString().trim();
        if (fullname.isEmpty()) {
            inputLayoutFullname.setError(getString(R.string.err_msg_fullname));
            requestFocus(etFullname);
            return false;
        } else {
            inputLayoutFullname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        email = etEmail.getText().toString();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(etEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword() {
        password = etPassword.getText().toString();

        if (password.length() < 6) {
            if (password.isEmpty()) {
                inputLayoutPassword.setError(getString(R.string.err_msg_pass_empty));
                requestFocus(etPassword);
                return false;
            }
            inputLayoutPassword.setError(getString(R.string.err_msg_pass_short));
            requestFocus(etPassword);
            return false;
        }
        else
            inputLayoutPassword.setErrorEnabled(false);

        return true;
    }

    private boolean validateCPassword() {
        cpassword = etCPassword.getText().toString();

        if (!cpassword.equals(password)) {
            inputLayoutCPassword.setError(getString(R.string.err_msg_cpass));
            requestFocus(etCPassword);
            return false;
        }
        else
            inputLayoutCPassword.setErrorEnabled(false);
        return true;
    }


    private void requestFocus(View view) {
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();

    }

    private void onSignUpSuccess() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String fullname = user.getString("fullname");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");


                        // Inserting row in users table
                        db.addUser(fullname, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname", fullname);
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_fullname:
                    validateName();
                    break;
                case R.id.et_password:
                    validatePassword();
                    break;
                case R.id.et_confirm_password:
                    validateCPassword();
                    break;
                case R.id.et_email:
                    validateEmail();
                    break;
            }
        }
    }

}
