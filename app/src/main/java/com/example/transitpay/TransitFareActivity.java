package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//TODO display different fare options in list view. Each elements are clickable and open a fragment

public class TransitFareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_fare);
    }
}