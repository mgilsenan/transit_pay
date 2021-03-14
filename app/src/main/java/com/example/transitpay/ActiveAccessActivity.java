package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

//TODO display user profile in list view.
public class ActiveAccessActivity extends AppCompatActivity {

    TextView email, password, name, phone, nfcStatus, paymentStatus, nfcTagId;

    TextView profile;
    Button mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_access);

        mainMenuButton=findViewById(R.id.mainMenuButton);
        profile = findViewById(R.id.userProfileTxt);

        displayProfile();

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenuActivity();
            }
        });

    }

    protected void displayProfile(){

        profile.append("phone: " + LoginActivity.getUser().toString() + "\n");
    }

    private void goToMainMenuActivity(){
        Intent intent= new Intent(ActiveAccessActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}