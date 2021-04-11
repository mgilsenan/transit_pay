package com.example.transitpay;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Integer.parseInt;

public class MainMenuActivity extends AppCompatActivity {
    protected Button triphistoryButton;
    protected Button locationButton;
    protected Button purchaseButton;
    protected Button citylinesButton;
    DatabaseReference childNode;
    private String remainingday="0";
    private String remainingticket="0";
    private TextView fareviewtext;
    private ImageButton fareviewchangebtn;




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
        fareviewtext = findViewById(R.id.fareviewtxt);
        fareviewchangebtn = findViewById(R.id.fareviewchangebtn);



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

        setRemainingfare();

        fareviewchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((fareviewtext.getText()).equals(remainingday + " Days Left")||(fareviewtext.getText()).equals(remainingday + " Day Left")){
                    if(parseInt(remainingticket)>1){
                        textflip();
                        fareviewtext.setText(remainingticket + " Trips Left");
                    }
                    else {
                        textflip();
                        fareviewtext.setText(remainingticket + " Trip Left");
                    }
                }
                else if((fareviewtext.getText()).equals(remainingticket + " Trips Left")||(fareviewtext.getText()).equals(remainingticket + " Trip Left")){
                    if(parseInt(remainingday)>1){
                        textflip();
                        fareviewtext.setText(remainingday + " Days Left");
                    }
                    else {
                        textflip();
                        fareviewtext.setText(remainingday + " Day Left");
                    }
                }
            }
        });






    }
    public void textflip(){
        fareviewtext.animate().rotationXBy(360f);

    }

    private void setRemainingfare() {
        childNode=FirebaseDatabase.getInstance().getReference().child("user").child(LoginActivity.getUser().getPhone());
        childNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("ticketsLeft").exists()){
                    remainingticket = snapshot.child("ticketsLeft").getValue().toString();
                    if(parseInt(remainingticket)>1){
                        fareviewtext.setText(remainingticket + " Trips Left");
                    }
                    else {
                        fareviewtext.setText(remainingticket + " Trip Left");
                    }

                }
                else {
                    fareviewtext.setText(remainingticket + " Trip Left");
                }

                if (snapshot.child("daysLeft").exists()) {
                    remainingday = snapshot.child("daysLeft").getValue().toString();
                    if(parseInt(remainingday)>1){
                        fareviewtext.setText(remainingday + " Days Left");
                    }
                    else {
                        fareviewtext.setText(remainingday + " Day Left");
                    }
                }
                else {
                    fareviewtext.setText(remainingday + " Day Left");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


}
