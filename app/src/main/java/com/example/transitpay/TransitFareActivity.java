package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

//TODO display different fare options in list view. Each elements are clickable and open a fragment

public class TransitFareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_fare);

        Button oneTrip, twoTrips, tenTrips, monthlyFare;

        oneTrip = findViewById(R.id.one_trip);
        twoTrips = findViewById(R.id.two_trip);
        tenTrips = findViewById(R.id.ten_trip);
        monthlyFare = findViewById(R.id.monthly_trip);

        oneTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TransitFareActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        twoTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TransitFareActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        tenTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TransitFareActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        monthlyFare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TransitFareActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

    }
}