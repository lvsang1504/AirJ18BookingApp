package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.LoginResponseListener;
import com.devpro.airj18bookingapp.listeners.PasswordResponseListener;
import com.devpro.airj18bookingapp.models.Response.PasswordResponse;
import com.devpro.airj18bookingapp.models.UserResponse;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;

public class NewPasswordActivity extends AppCompatActivity {
    EditText editTextNewPassword,editTextNewPassword2;
    Button buttonConfirmNewPassword;
    String gmail="";
    RequestManager requestManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        gmail=getIntent().getStringExtra("gmail");
        requestManager = new RequestManager(this);
        setControl();
        setEvent();
    }

    private void setEvent() {
        buttonConfirmNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(editTextNewPassword.getText())||TextUtils.isEmpty(editTextNewPassword2.getText())) {
                    Toast.makeText(NewPasswordActivity.this, "Nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!editTextNewPassword.getText().toString().trim().equals(editTextNewPassword2.getText().toString().trim())) {
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
               //Doi password
                requestManager.resetPassword(passwordResponseListener,gmail,editTextNewPassword.getText().toString());

            }
        });
    }

    private void setControl() {
        editTextNewPassword=findViewById(R.id.editTextNewPassword);
        editTextNewPassword2=findViewById(R.id.editTextNewPassword2);
        buttonConfirmNewPassword=findViewById(R.id.buttonConfirmNewPassword);
    }
    private final PasswordResponseListener passwordResponseListener = new PasswordResponseListener() {
        @Override
        public void didFetch(PasswordResponse passwordResponse, String message) {
            Toast.makeText(NewPasswordActivity.this,"Change password successfully",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(NewPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    };
}