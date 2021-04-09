package com.example.transitpay;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NfcAdapter;
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
    NfcAdapter nfc;

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
                gotoGetStarted();
               //goToInfoCardActivity();
                gotoGetStarted();
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(nfc==null)
        {
            nfc = NfcAdapter.getDefaultAdapter(this);
        }
        nfc.disableForegroundDispatch(this);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(nfc==null)
        {
            nfc = NfcAdapter.getDefaultAdapter(this);
        }
        nfc.disableForegroundDispatch(this);
    }

    private void gotoGetStarted(){
        Intent intent= new Intent(MainMenuActivity.this, GetStarted.class);
        startActivity(intent);
    }
    private void goToTripHistoryActivity(){
//        String phone_number=getIntent().getStringExtra("Phone number");
        Intent intent= new Intent(MainMenuActivity.this, TripHistoryActivity.class);
//        intent.putExtra("Phone number", phone_number);
//        Toast.makeText(this, "The intent MAINMENU phoneNumber"+phone_number,
//                Toast.LENGTH_SHORT).show();
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


}
