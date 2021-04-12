package com.example.transitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// TODO Refine UI

public class SignUpActivity extends AppCompatActivity {

    Button loginBtn, continueBtn;
    TextView welcome;
    TextInputLayout email, password, name, phone;
    FirebaseAuth fAuth;
//    String userID;
    NfcAdapter nfc;
    private static Boolean flag; // if the phone number exists in the database it sets to true
    final static String TAG = "SignUpActivity";


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private static final int passwordLength = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // hook
        setUp();

        // go to log in page
        loginBtnAction();

        // save data in Firebase
        continueBtnAction();

        //get firebaseAuthentication
        fAuth = FirebaseAuth.getInstance();

    }

    public void setUp(){
        loginBtn = findViewById(R.id.loginBtn);
        continueBtn = findViewById(R.id.continueBtn);
        welcome = findViewById(R.id.welcome);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

    }

    public void continueBtnAction(){
        continueBtn.setOnClickListener(new View.OnClickListener() {
            // save user obj in realtime database: phone number is unique user id
            // if same phone number user info over write
            @Override
            public void onClick(View v) {

                Toast toast;
                if (!isEmailValid() | !isNameValid() | !isPasswordValid() | !isPhoneValid()){
                    toast = Toast.makeText(SignUpActivity.this, "Unsuccessful", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("user");


                    String nameStr = name.getEditText().getText().toString().trim();
                    String emailStr = email.getEditText().getText().toString().trim();
                    String passwordStr = password.getEditText().getText().toString().trim();
                    String phoneStr = phone.getEditText().getText().toString().trim();

                    Query checkUserPhone = reference.orderByChild("phone").equalTo(phoneStr);

                    Log.d(TAG, "the phone number entered outside of even listenner"+phoneStr);
                    phone.setError(null);
                    email.setError(null);

                    checkUserPhone.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d(TAG, "the phone number entered is found"+phoneStr);
                                phone.setError("phone number is  already taken by other user \n" +
                                        "delete existing account and try again");
                                phone.requestFocus();

                            }
                            else{
                                Log.d(TAG, "the phone number entered is not found"+phoneStr);

                                Query checkUserEmail = reference.orderByChild("email").equalTo(emailStr);
                                checkUserEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            Log.d(TAG,"the email is found " + emailStr);
                                            email.setError("Email is already taken by other user \n" +
                                                    "delete existing account and try again");
                                            email.requestFocus();
                                        }else{
                                            Log.d(TAG, "the email entered is not found "+emailStr);
                                            fAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                Toast.makeText(SignUpActivity.this, "Request sent, Please Check Your Email for Verification",
                                                                        Toast.LENGTH_LONG).show();
                                                                User user = new User(nameStr, emailStr, phoneStr);
                                                                user.setPassword(passwordStr);

                                                                reference.child(phoneStr).setValue(user);
                                                                reference.child(phoneStr).child("loginBefore").getRef().setValue("FALSE");
                                                                reference.child(phoneStr).child("emailVerified").getRef().setValue("FALSE");
                                                                //Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                Intent intent = new Intent(SignUpActivity.this, GetStarted.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                            else {
                                                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                                        Toast.LENGTH_LONG).show();
                                                            }

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // do nothing
                                    }
                                });

                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // do nothing.
                        }

                    });

                }

            }
        });

    }

    private Boolean isNameValid(){
        String nameStr = name.getEditText().getText().toString();

        if (nameStr.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isEmailValid(){
        String emailStr = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailStr.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }else if(!emailStr.matches(emailPattern)){
            email.setError("Invalid pattern");
            return false;
        }
        else
        {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isPasswordValid(){
        String passwordStr = password.getEditText().getText().toString();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                // "(?=.*[@#$%^&+=!])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 4 characters
                "$";

        if (passwordStr.isEmpty()){
            password.setError("Field cannot be empty");

            return false;
        }else if(!passwordStr.matches(passwordPattern)){
            password.setError("Include at least 6 characters with no space between ");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isPhoneValid(){
        String phoneStr = phone.getEditText().getText().toString();


        if (phoneStr.isEmpty()){
            phone.setError("Field cannot be empty");

            return false;
        }else{
//            phone.setError(null);
//            phone.setErrorEnabled(false);
            return true;
        }
    }

    public void loginBtnAction(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            // go back to login activity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (SignUpActivity.this, LoginActivity.class);

                // array size needs to be exactly same as the number of the elements
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String> (phone, "phone_number_trans");
                pairs[1] = new Pair<View, String> (password, "password_trans");
                pairs[2] = new Pair<View, String> (continueBtn, "continue_trans");
                pairs[3] = new Pair<View, String> (loginBtn, "signUp_login_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }
}