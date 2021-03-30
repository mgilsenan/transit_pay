package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    TextView profile;
    Button mainMenuButton;
    TextInputEditText name;
    TextInputEditText phone;
    TextInputEditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // hook
        profile = findViewById(R.id.userProfileTxt);
        mainMenuButton=findViewById(R.id.saveBn);
        name = findViewById(R.id.nameTxt);
        phone = findViewById(R.id.phoneTxt);
        email = findViewById(R.id.emailTxt);

        displayProfile();

        // disable editing  
        name.setInputType(InputType.TYPE_NULL);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenuActivity();
            }
        });
    }


    protected void displayProfile(){

        name.setText(LoginActivity.getUser().getName());
        phone.setText(LoginActivity.getUser().getPhone());
        email.setText(LoginActivity.getUser().getEmail());

    }

    private void goToMainMenuActivity(){
        Intent intent= new Intent(ProfileActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}