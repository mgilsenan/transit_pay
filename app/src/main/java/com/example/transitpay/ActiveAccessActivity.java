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

        profile.append("phone: " + LoginActivity.getUser().toString() + "\n");
    }
}