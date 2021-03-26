package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//TODO display different fare options in list view. Each elements are clickable and open a fragment

public class TransitFareActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_fare);

        Button oneTrip, twoTrips, tenTrips, threeDayPass, weeklyPass, monthlyPass;

        oneTrip = findViewById(R.id.one_trip);
        twoTrips = findViewById(R.id.two_trip);
        tenTrips = findViewById(R.id.ten_trip);
        threeDayPass = findViewById(R.id.threeday_trip);
        weeklyPass = findViewById(R.id.weekly_trip);
        monthlyPass = findViewById(R.id.monthly_trip);

        oneTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignFareType("1");
                Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

                String oneTrip="Single Trip Fare";

                String price="$3.50";

                Bundle bundle = new Bundle();

                bundle.putString("fare",oneTrip);

                bundle.putString("price",price);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        twoTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignFareType("2");

                Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

                String twoTrip="Two Trip Fare";

                String price="$6.50";

                Bundle bundle = new Bundle();

                bundle.putString("fare",twoTrip);

                bundle.putString("price",price);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        tenTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignFareType("10");
                Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

                String tenTrip="10 Trip Fare";

                String price="$29.50";

                Bundle bundle = new Bundle();

                bundle.putString("fare",tenTrip);

                bundle.putString("price",price);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        threeDayPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignFareType("3Day");
                Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

                String threeTrip="Three Day Fare";

                String price="$20.00";

                Bundle bundle = new Bundle();

                bundle.putString("fare",threeTrip);

                bundle.putString("price",price);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        weeklyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignFareType("W");
                Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

                String weekFare = "Weekly Fare";

                String price="$27.25";

                Bundle bundle = new Bundle();

                bundle.putString("fare",weekFare);

                bundle.putString("price",price);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        monthlyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignFareType("M");
                Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

                String weekFare = "Monthly Fare";

                String price="$88.50";

                Bundle bundle = new Bundle();

                bundle.putString("fare",weekFare);

                bundle.putString("price",price);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }

    private void assignFareType(String fareType) {
        rootNode = FirebaseDatabase.getInstance();
        String phoneNumber = null;
        if (LoginActivity.getUser() != null) {
            User user = LoginActivity.getUser();
            phoneNumber = user.phone;
        }
        reference = rootNode.getReference("user/"+phoneNumber);
        reference.child("Fare Type").setValue(fareType);
    }
}