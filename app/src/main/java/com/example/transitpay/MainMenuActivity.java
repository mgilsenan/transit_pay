package com.example.transitpay;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {
    protected Button triphistoryButton;
    protected Button locationButton;
    protected Button purchaseButton;
    protected Button citylinesButton;
    protected Button activateButton;


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


    }
    private void goToTripHistoryActivity(){
        String phone_number=getIntent().getStringExtra("Phone number");
        Intent intent= new Intent(MainMenuActivity.this, TripHistoryActivity.class);
        intent.putExtra("Phone number", phone_number);
        startActivity(intent);
    }
    private void goToLocationActivity(){
        
        Intent intent= new Intent(MainMenuActivity.this, MapsActivity.class);
        startActivity(intent);

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


    // Logout option------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set the menu icon of the page with layout xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null; // value of the intent is depends on the user selected button option
        switch(item.getItemId()){
            case R.id.Logout:
                Toast.makeText(this, "Logged out from account", Toast.LENGTH_LONG).show();
                // destroy local user obj and return to login activity
                LoginActivity.getUser().setPhone("");
                LoginActivity.getUser().setName("");
                LoginActivity.getUser().setEmail("");
                intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.Profile:
                intent= new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
