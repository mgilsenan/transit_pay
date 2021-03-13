package com.example.transitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


//TODO use phone number instead of email when login
// Logout button and forget my password features.

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static User user ;

    Button callSignUpBtn, continueBtn;
    TextView welcome;
    TextInputLayout phone, password;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUp();

        callSignUpBtnAction();

        continueBtnAction();




    }

    protected void setUp(){
        //Hooks
        callSignUpBtn = findViewById(R.id.signUpBtn);
        continueBtn = findViewById(R.id.loginContinueBtn);
        welcome = findViewById(R.id.welcome);
        phone = findViewById(R.id.loginPhone);
        password = findViewById(R.id.loginPassword);
        user = new User();

    }

    protected void callSignUpBtnAction(){
        callSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this, SignUpActivity.class);

                // array size must be same as the number of the elements
                //TODO fixed hardcoded
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String> (phone, "phone_number_trans");
                pairs[1] = new Pair<View, String> (password, "password_trans");
                pairs[2] = new Pair<View, String> (welcome, "welcome_trans");
                pairs[3] = new Pair<View, String> (continueBtn, "continue_trans");
                pairs[4] = new Pair<View, String> (callSignUpBtn, "signUp_login_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });

    }

    protected void continueBtnAction(){

        // @TODO Main display activity
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                if (!validatePhone() | !validatePassword()) {
                    toast = Toast.makeText(LoginActivity.this, "Unsuccessful", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    isUser();
                }

            }
        });


    }

    private Boolean validatePhone() {
        String val = phone.getEditText().getText().toString();
        if (val.isEmpty()) {
            phone.setError("Field cannot be empty");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void isUser() {
        //progressBar.setVisibility(View.VISIBLE);
        final String userEnteredPhone = phone.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();
        reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredPhone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    phone.setError(null);
                    phone.setErrorEnabled(false);
                    //TODO we might change the line below------------------------------------------------------------
                    String passwordFromDB = dataSnapshot.child(userEnteredPhone).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        Log.d(TAG, "password Equal " + userEnteredPhone);

                        phone.setError(null);
                        phone.setErrorEnabled(false);
                        String nameFromDB = dataSnapshot.child(userEnteredPhone).child("name").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredPhone).child("email").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredPhone).child("phone").getValue(String.class);

                        // save user phone number upon loggin
                        user.copy(new User(nameFromDB, emailFromDB, phoneNoFromDB,passwordFromDB ));

                        Intent intent = new Intent(LoginActivity.this, ActiveAccessActivity.class);

                        startActivity(intent);
                        finish();
                    } else {
                        Log.d(TAG, "password not equal " + userEnteredPhone);

                       // progressBar.setVisibility(View.GONE);
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    Log.d(TAG, "no snapshot " + userEnteredPhone);

                    //progressBar.setVisibility(View.GONE);
                    phone.setError("No such User exist");
                    phone.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing.
            }

        });
    }

    public static User getUser(){return user == null ? null: user;}
}