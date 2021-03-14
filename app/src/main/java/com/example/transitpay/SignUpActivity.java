package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// TODO Refine UI

public class SignUpActivity extends AppCompatActivity {

    Button loginBtn, continueBtn;
    TextView welcome;
    TextInputLayout email, password, name, phone;

    FirebaseDatabase rootNode;
    DatabaseReference childNode;

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
                    childNode = rootNode.getReference("user");

                    String nameStr = name.getEditText().getText().toString();
                    String emailStr = email.getEditText().getText().toString();
                    String passwordStr = password.getEditText().getText().toString();
                    String phoneStr = phone.getEditText().getText().toString();


                    User user = new User(nameStr, emailStr,  phoneStr, passwordStr);

                    childNode.child(phoneStr).setValue(user);

                    toast = Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_LONG);
                    toast.show();

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                "(?=.*[@#$%^&+=!])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (passwordStr.isEmpty()){
            password.setError("Field cannot be empty");

            return false;
        }else if(!passwordStr.matches(passwordPattern)){
            password.setError("Invalid pattern");
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
            phone.setError(null);
            phone.setErrorEnabled(false);
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
                //TODO fixed hardcoded
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String> (phone, "phone_number_trans");
                pairs[1] = new Pair<View, String> (password, "password_trans");
                pairs[2] = new Pair<View, String> (welcome, "welcome_trans");
                pairs[3] = new Pair<View, String> (continueBtn, "continue_trans");
                pairs[4] = new Pair<View, String> (loginBtn, "signUp_login_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }
}