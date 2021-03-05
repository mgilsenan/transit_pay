package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

//TODO display user profile in list view.
public class ActiveAccessActivity extends AppCompatActivity {

    TextView email, password, name, phone, nfcStatus, paymentStatus, nfcTagId;

    TextView profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_access);

        profile = findViewById(R.id.userProfileTxt);

        displayProfile();


    }

    protected void displayProfile(){
        if (getIntent().hasExtra("name")){
            profile.append("name: " + getIntent().getStringExtra("name") + "\n");
        }
        if (getIntent().hasExtra("email")){
            profile.append("email: " + getIntent().getStringExtra("email") + "\n");
        }
        if (getIntent().hasExtra("phone")){
            profile.append("phone: " + getIntent().getStringExtra("phone") + "\n");
        }
    }
}