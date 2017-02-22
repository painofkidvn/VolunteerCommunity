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

import com.hat_dtu.volunteercommunity.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private String email, password;
    private Button btnLogin;
    private TextInputLayout inputLayoutPassword, inputLayoutEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText)findViewById(R.id.et_email_l);
        etPassword = (EditText)findViewById(R.id.et_password_l);
        inputLayoutEmail = (TextInputLayout)findViewById(R.id.input_layout_email_l);
        inputLayoutPassword = (TextInputLayout)findViewById(R.id.input_layout_password_l);

        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

        if (password.isEmpty()) {
                inputLayoutPassword.setError(getString(R.string.err_msg_pass_empty));
                requestFocus(etPassword);
                return false;
        }
        else
            inputLayoutPassword.setErrorEnabled(false);

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.et_email:
                    validateEmail();
                    break;
                case R.id.et_password:
                    validatePassword();
                    break;

            }
        }
    }
}
