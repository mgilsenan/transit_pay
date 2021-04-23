package com.example.transitpay.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.transitpay.R;

public class InfoActivateCard extends AppCompatActivity {
    TextView titleText;
    TextView stepsText;
    Button nextActivateButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activate_activity);
        setupUI();


    }
    private void setupUI(){
        titleText=findViewById(R.id.titleactivateTextView);
        stepsText=findViewById(R.id.infoactivateTextView);
        nextActivateButton=findViewById(R.id.nextinfoButton);
        nextActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivateCard();
            }
        });
    }
    private void goToActivateCard(){
      //  String phone_number = getIntent().getStringExtra("Phone number");
//        Toast.makeText(this, "The intent INFOOO ACTIVITY phoneNumber"+phone_number,
//                Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(InfoActivateCard.this, ActivateCardActivity.class);
//        intent.putExtra("Phone number", phone_number);
        startActivity(intent);

    }
}
