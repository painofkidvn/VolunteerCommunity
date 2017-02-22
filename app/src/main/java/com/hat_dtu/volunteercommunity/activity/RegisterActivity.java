package com.hat_dtu.volunteercommunity.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hat_dtu.volunteercommunity.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullname, etPassword, etCPassword, etEmail, etPhone;
    private Button btnSignUp;
    private String fullname, password, cpassword, email, phone;
    private TextInputLayout inputLayoutFullname, inputLayoutPassword, inputLayoutCPassword, inputLayoutEmail, inputLayoutPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputLayoutFullname = (TextInputLayout) findViewById(R.id.input_layout_fullname);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutCPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        etFullname = (EditText) findViewById(R.id.et_fullname);
        etPassword = (EditText) findViewById(R.id.et_password);
        etCPassword = (EditText) findViewById(R.id.et_confirm_password);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);

        etFullname.addTextChangedListener(new MyTextWatcher(etFullname));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        etCPassword.addTextChangedListener(new MyTextWatcher(etCPassword));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etPhone.addTextChangedListener(new MyTextWatcher(etPhone));

        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
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
        if(!validatePhone())
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

    private boolean validatePhone() {

        phone = etPhone.getText().toString();
        if (phone.isEmpty() || phone.length() > 11) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(etPhone);
            return false;
        }
        else
            inputLayoutPhone.setErrorEnabled(false);
        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void onSignUpSuccess() {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
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
                case R.id.et_phone:
                    validatePhone();
                    break;
            }
        }
    }

}
