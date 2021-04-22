package com.example.transitpay;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transitpay.Authenticate.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Integer.parseInt;

public class MainMenuActivity extends AppCompatActivity {

    private static String TAG = "MainMenuActivity";

    protected Button triphistoryButton;
    protected Button locationButton;
    protected Button purchaseButton;
    protected Button citylinesButton;

    DatabaseReference childNode;
    private String remainingday="0";
    private String remainingticket="0";
    private TextView fareviewtext;
    private ImageButton fareviewchangebtn;

//    protected Button activateButton;
//    NfcAdapter nfc=null;




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


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(nfc==null)
//        {
//            nfc = NfcAdapter.getDefaultAdapter(this);
//        }
//        nfc.disableForegroundDispatch(this);
//        final Intent[] intent = {null};
//        intent[0] = new Intent(MainMenuActivity.this, LoginActivity.class);
//        startActivity(intent[0]);
//        finish();
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(nfc==null)
//        {
//            nfc = NfcAdapter.getDefaultAdapter(this);
//        }
//        nfc.disableForegroundDispatch(this);
//
//    }

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


    private void  goToActivateCardActivity(){
        Intent intent= new Intent(MainMenuActivity.this, ActivateCardActivity.class);
        startActivity(intent);
    }


    // Logout option------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set the menu icon of the page with layout xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final Intent[] intent = {null}; // value of the intent is depends on the user selected button option
//        if(nfc==null)
//        {
//            nfc = NfcAdapter.getDefaultAdapter(this);
//        }
//        nfc.disableForegroundDispatch(this);
        switch(item.getItemId()){
            case R.id.logout:
                Toast.makeText(this, "Logged out from account", Toast.LENGTH_LONG).show();
                final String phonStr = LoginActivity.getUser().getPhone();
                FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.child(phonStr).exists() ) {

                            // reset the flag
                            FirebaseDatabase.getInstance().getReference("user").child(phonStr).child("loginBefore").setValue("FALSE");

                            // destroy local user obj and return to login activity
                            LoginActivity.getUser().setPhone("");
                            LoginActivity.getUser().setName("");
                            LoginActivity.getUser().setEmail("");
                            LoginActivity.clearUser(); // removed the user in the preference file

                            SharedPreferences pref = getSharedPreferences(LoginActivity.myPreference, Context.MODE_PRIVATE);
                            String empty = pref.getString(LoginActivity.userPhone, "");
                            String empty2 = pref.getString(LoginActivity.userPassword, "");
                            Log.d(TAG,"the value of shared preference after LogOut is: " + empty );
                            Log.d(TAG,"the value of shared preference after LogOut is: " + empty2 );
                            intent[0] = new Intent(MainMenuActivity.this, LoginActivity.class);
                            startActivity(intent[0]);
                            finish();
                        }else{
                            Toast.makeText(MainMenuActivity.
                                            this, "Phone number is not linked to an account",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            case R.id.profile:
                intent[0]= new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(intent[0]);
                return true;
            case R.id.cardActivation:
                intent[0]= new Intent(MainMenuActivity.this, InfoActivateCard.class);
                startActivity(intent[0]);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
