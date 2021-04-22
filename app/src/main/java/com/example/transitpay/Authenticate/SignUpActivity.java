package com.example.transitpay.Authenticate;

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

import com.example.transitpay.GetStarted;
import com.example.transitpay.R;
import com.example.transitpay.Model.User;
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
    NfcAdapter nfc;
    final static String TAG = "SignUpActivity";


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private static final int phoneNumLength = 10;

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
            // if phone number is taken by other accounts deny
            @Override
            public void onClick(View v) {

                Toast toast;
                if (!isEmailValid() | !isNameValid() | !isPasswordValid() | !isPhoneValid()){
                    toast = Toast.makeText(SignUpActivity.this, "Unsuccessful", Toast.LENGTH_LONG);
                    toast.show();
                }else{

                    reference = FirebaseStrings.getRef();


                    String nameStr = name.getEditText().getText().toString().trim();
                    String emailStr = email.getEditText().getText().toString().trim();
                    String passwordStr = password.getEditText().getText().toString().trim();
                    String phoneStr = phone.getEditText().getText().toString().trim();

                    Query checkUserPhone = reference.orderByChild(FirebaseStrings.getFirebasePhone()).equalTo(phoneStr);

                    Log.d(TAG, "the phone number entered outside of even listenner"+phoneStr);


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

                                Query checkUserEmail = reference.orderByChild(FirebaseStrings.getFirebaseEmail()).equalTo(emailStr);
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
                                                                // text field is error free at this point
                                                                phone.setErrorEnabled(false);
                                                                email.setErrorEnabled(false);
                                                                password.setErrorEnabled(false);

                                                                Toast.makeText(SignUpActivity.this, "Request sent, Please Check Your Email for Verification",
                                                                        Toast.LENGTH_LONG).show();

                                                                // create local user obj
                                                                User user = new User(nameStr, emailStr, phoneStr);
                                                                user.setPassword(passwordStr); // only for debugging purpose

                                                                // create a new user in real time database
                                                                reference.child(phoneStr).setValue(user);
                                                                reference.child(phoneStr).child(FirebaseStrings.getFirebaseLoginBefore()).getRef().setValue("FALSE");
                                                                reference.child(phoneStr).child(FirebaseStrings.getGetFirebaseEmailVerified()).getRef().setValue("FALSE");

                                                                // go to next activity
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
            return true;
        }
    }


    private Boolean isPasswordValid(){
        String passwordStr = password.getEditText().getText().toString();
        String passwordPattern = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
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
            return true;
        }
    }

    private Boolean isPhoneValid(){
        String phoneStr = phone.getEditText().getText().toString();


        if (phoneStr.isEmpty()){
            phone.setError("Field cannot be empty");

            return false;
        }else if(phoneStr.length() != phoneNumLength) {
            phone.setError("phone number should be 10 digits long\n ex.4381109111");
            return false;
        }else
        {
            phone.setError(null);
            return true;
        }
    }

    // text boxes are shared with login activity
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