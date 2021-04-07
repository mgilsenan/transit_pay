package com.example.transitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        rootNode = FirebaseDatabase.getInstance();

        String phoneNumber = null;
        if (LoginActivity.getUser() != null) {
            User user = LoginActivity.getUser();
            phoneNumber = user.phone;
        }

        reference = rootNode.getReference("user/"+phoneNumber);

        oneTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("ticketsLeft").exists()) {
                                Integer value = dataSnapshot.child("ticketsLeft").getValue(Integer.class);
                                boolean isZero = value.equals(0);
                                //reference.child("Zero").setValue(isZero);
                                if(isZero){
                                    singleFareCheckout();
                                } else{
                                    Toast.makeText(TransitFareActivity.this, "Please use your remaining tickets before making a purchase", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                singleFareCheckout();
                            }
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        twoTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("ticketsLeft").exists()) {
                            Integer value = dataSnapshot.child("ticketsLeft").getValue(Integer.class);
                            boolean isZero = value.equals(0);
                            //reference.child("Zero").setValue(isZero);
                            if(isZero){
                                twoFareCheckout();
                            } else{
                                Toast.makeText(TransitFareActivity.this, "Please use your remaining tickets before making a purchase", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            twoFareCheckout();
                        }
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tenTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("ticketsLeft").exists()) {
                            Integer value = dataSnapshot.child("ticketsLeft").getValue(Integer.class);
                            boolean isZero = value.equals(0);
                            //reference.child("Zero").setValue(isZero);
                            if(isZero){
                                tenFareCheckout();
                            } else{
                                Toast.makeText(TransitFareActivity.this, "Please use your remaining tickets before making a purchase", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            tenFareCheckout();
                        }
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        threeDayPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("daysLeft").exists()) {
                            Integer value = dataSnapshot.child("daysLeft").getValue(Integer.class);
                            boolean isZero = value.equals(0);
                            //reference.child("Zero").setValue(isZero);
                            if(isZero){
                                threeDayFareCheckout();
                            } else{
                                Toast.makeText(TransitFareActivity.this, "Please use your remaining tickets before making a purchase", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            threeDayFareCheckout();
                        }
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        weeklyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("daysLeft").exists()) {
                            Integer value = dataSnapshot.child("daysLeft").getValue(Integer.class);
                            boolean isZero = value.equals(0);
                            //reference.child("Zero").setValue(isZero);
                            if(isZero){
                                weeklyFareCheckout();
                            } else{
                                Toast.makeText(TransitFareActivity.this, "Please use your remaining tickets before making a purchase", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            weeklyFareCheckout();
                        }
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        monthlyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("daysLeft").exists()) {
                            Integer value = dataSnapshot.child("daysLeft").getValue(Integer.class);
                            boolean isZero = value.equals(0);
                            //reference.child("Zero").setValue(isZero);
                            if(isZero){
                                monthlyFareCheckout();
                            } else{
                                Toast.makeText(TransitFareActivity.this, "Please use your remaining tickets before making a purchase", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            monthlyFareCheckout();
                        }
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void monthlyFareCheckout() {
        Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

        String weekFare = "Monthly Fare";

        String price="88.00";

        Bundle bundle = new Bundle();

        bundle.putString("fare",weekFare);

        bundle.putString("price",price);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void weeklyFareCheckout() {
        Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

        String weekFare = "Weekly Fare";

        String price="27.00";

        Bundle bundle = new Bundle();

        bundle.putString("fare",weekFare);

        bundle.putString("price",price);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void threeDayFareCheckout() {
        Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

        String threeTrip="Three Day Fare";

        String price="20.00";

        Bundle bundle = new Bundle();

        bundle.putString("fare",threeTrip);

        bundle.putString("price",price);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void tenFareCheckout() {
        Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

        String tenTrip="10 Trip Fare";

        String price="29.00";

        Bundle bundle = new Bundle();

        bundle.putString("fare",tenTrip);

        bundle.putString("price",price);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void twoFareCheckout() {
        Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

        String twoTrip="Two Trip Fare";

        String price="6.00";

        Bundle bundle = new Bundle();

        bundle.putString("fare",twoTrip);

        bundle.putString("price",price);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void singleFareCheckout() {
        Intent intent= new Intent(TransitFareActivity.this, CheckoutActivity.class);

        String oneTrip="Single Trip Fare";

        String price="3.00";

        Bundle bundle = new Bundle();

        bundle.putString("fare",oneTrip);

        bundle.putString("price",price);

        intent.putExtras(bundle);

        startActivity(intent);
    }

}