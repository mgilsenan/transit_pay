package com.example.transitpay;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {
    protected Button triphistoryButton;
    protected Button locationButton;
    protected Button purchaseButton;
    protected Button citylinesButton;
    protected Button activateButton;
    protected Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupUI();
    }
    private void setupUI(){
        triphistoryButton=findViewById(R.id.triphistoryButton);
        locationButton=findViewById(R.id.LocationButton);
        citylinesButton=findViewById(R.id.citylinesButton);
        purchaseButton=findViewById(R.id.purchasefareButton);
        activateButton = findViewById(R.id.activationcardButton);
        profileButton = findViewById(R.id.profileBtn);

        triphistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTripHistoryActivity();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocationActivity();
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPurchaseActivity();
            }
        });

        citylinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCityLinesActivity();
            }
        });
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivateCardActivity();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment
                setFragment(new ProfileFragment());
            }
        });

    }
    private void goToTripHistoryActivity(){
        Intent intent= new Intent(MainMenuActivity.this, TripHistoryActivity.class);
        startActivity(intent);
    }
    private void goToLocationActivity(){

    }
    private void goToPurchaseActivity(){
        Intent intent= new Intent(MainMenuActivity.this, TransitFareActivity.class);
        startActivity(intent);
    }
    private void  goToCityLinesActivity(){
        Intent intent= new Intent(MainMenuActivity.this, CitylinesActivity.class);
        startActivity(intent);
    }
    private void  goToActivateCardActivity(){
        Intent intent= new Intent(MainMenuActivity.this, ActivateCardActivity.class);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}
