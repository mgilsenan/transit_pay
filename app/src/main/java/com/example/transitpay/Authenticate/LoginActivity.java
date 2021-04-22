package com.example.transitpay.Authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transitpay.InfoActivateCard;
import com.example.transitpay.MainMenuActivity;
import com.example.transitpay.R;
import com.example.transitpay.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


//TODO use phone number instead of email when login
// Logout button and forget my password features.

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // local user
    public static User user ;
    public static SharedPreferences pref;
    private static boolean userExist; // if user Login before true
    public static final String myPreference = "myPreference";
    public static final String userPhone = "serPhoneKey";
    public static final String userPassword = "userPasswordKey";
    private String phoneStr;
    private String passwordStr;
    private static final int phoneNumLength = 10;



    // UI
    Button callSignUpBtn, continueBtn,forgetPasswordBtn;
    TextView welcome;
    TextInputLayout phone, password;

    // firebase
    DatabaseReference reference;
    FirebaseAuth fAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUp();

    }

    protected void setUp(){
        //Hooks
        callSignUpBtn = findViewById(R.id.signUpBtn);
        continueBtn = findViewById(R.id.loginContinueBtn);
        forgetPasswordBtn=findViewById(R.id.forgetPasswordBtn);
        phone = findViewById(R.id.loginPhone);
        password = findViewById(R.id.loginPassword);
        user = new User();
        fAuth = FirebaseAuth.getInstance();
        reference = FirebaseStrings.getRef();

    }


    // New account is taken to Info activity
    private void checkFirstTimeUser(String phoneNumber) {
//        reference = FirebaseStrings.getRef();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phoneNumber).child(FirebaseStrings.getGetFirebaseValidCard()).exists()) {
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(LoginActivity.this, InfoActivateCard.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    // UI elements shared with Signup
    protected void callSignUpBtnAction(){
        callSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this, SignUpActivity.class);

                // array size must be same as the number of the elements
                //TODO fixed hardcoded
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String> (phone, "phone_number_trans");
                pairs[1] = new Pair<View, String> (password, "password_trans");
                pairs[2] = new Pair<View, String> (continueBtn, "continue_trans");
                pairs[3] = new Pair<View, String> (callSignUpBtn, "signUp_login_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });

    }

    // If the user's info is saved in shared preference file then automatically sign in
    @Override
    protected void onResume() {
        super.onResume();
        callSignUpBtnAction();
        forgetPasswordBtnAction();
        pref = getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        String empty = pref.getString(userPhone, "");
        Log.d(TAG, "the value of empty is: " + empty);
        if (!(empty.matches(""))){
            Log.d(TAG,"not empty");
            phoneStr = pref.getString(userPhone,"");
            passwordStr = pref.getString(userPassword, "");
            setUserExist(true);
            isUser(getUserExist());
        }else{
            Log.d(TAG," empty");
            continueBtnAction();
        }
    }

    protected void continueBtnAction(){

        Log.d(TAG, "Log in button clicked");
        // @TODO Main display activity
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                if (!validatePhone() | !validatePassword()) {
                    toast = Toast.makeText(LoginActivity.this, "Unsuccessful", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Log.d(TAG, "check User exit " );
                    setUserExist(false);
                    isUser(getUserExist());
                }

            }
        });

    }

    protected void forgetPasswordBtnAction(){
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail =  new EditText(v.getContext());
                AlertDialog.Builder passwordRestDialog = new AlertDialog.Builder(v.getContext());
                passwordRestDialog.setTitle("Reset Password?");
                passwordRestDialog.setMessage("Enter Your Email to Receive Reset Link. ");
                passwordRestDialog.setView(resetMail);

                passwordRestDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        if(mail.matches("")){
                            Toast.makeText(LoginActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    clearUser(); // if the user reset password then user need to login again
                                    Toast.makeText(LoginActivity.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Error! Reset Link is Not Sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                passwordRestDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                passwordRestDialog.create().show();
            }
        });

    }


    // check valid phone
    private Boolean validatePhone() {
        Log.d(TAG, "validate phone: ");
        String val = phone.getEditText().getText().toString();
        if (val.isEmpty()) {
            phone.setError("Field cannot be empty");
            Log.d(TAG, "validate phone: " + val);
            return false;
        } else if(val.length() != phoneNumLength) {
            phone.setError("phone number should be 10 digits long\n ex.4381109111");
            return false;
        }else {
            phone.setError(null);
            return true;
        }
    }

    // check valid password
    private Boolean validatePassword() {
        Log.d(TAG, "validate password: " );
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            Log.d(TAG, "validate password: " + val);
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }


    private void isUser(boolean existingUser) {

        String userEnteredPhone ;
        String userEnteredPassword ;

        // via shared preference
        if(existingUser){
            Log.d(TAG,"existing user checked");

            userEnteredPhone = phoneStr;
            userEnteredPassword = passwordStr;
            phone.getEditText().setText(userEnteredPhone);
            password.getEditText().setText(userEnteredPassword);
        }else{
            Log.d(TAG,"shared preference is empty");
            userEnteredPhone = phone.getEditText().getText().toString().trim();
            userEnteredPassword = password.getEditText().getText().toString().trim();
        }

        Log.d(TAG,userEnteredPhone);
        Log.d(TAG,userEnteredPassword);

        Query checkUser = reference.orderByChild(FirebaseStrings.getFirebasePhone()).equalTo(userEnteredPhone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //TODO we might change the line below------------------------------------------------------------
                    String phoneFromDB = dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebasePhone()).getValue(String.class);
                    if (phoneFromDB.equals(userEnteredPhone)) {
                        //Log.d(TAG, "password Equal " + userEnteredPhone);


                        String emailFromDB = dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseEmail()).getValue(String.class).trim();

                        fAuth.signInWithEmailAndPassword(emailFromDB,userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // use the password given by the user and user the email that is stored in the database based on given phone number
                                // if the password in database does not match with user provided password then fail.
                                // if the email stored in the authentication match with real time database email then success fully login
                                if(dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseLoginBefore()).getValue(String.class).matches("TRUE")) Log.d(TAG, "loginBefore");

                                //If the current user updated email address. But verification email is not sent by firebase auth
                                if(task.isSuccessful() || dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseLoginBefore()).getValue(String.class).matches("TRUE")){

                                    // first time user need to verify the email
                                    if(fAuth.getCurrentUser().isEmailVerified() || dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseLoginBefore()).getValue(String.class).matches("TRUE")){
                                        phone.setErrorEnabled(false);
                                        password.setErrorEnabled(false);

                                        Log.d(TAG, "login success  " + userEnteredPhone);

                                        String nameFromDB = dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseName()).getValue(String.class);
                                        String phoneNoFromDB = dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebasePhone()).getValue(String.class);

                                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                        intent.putExtra("Phone number", phoneNoFromDB);
                                        // save user phone number upon loggin
                                        user.copy(new User(nameFromDB, emailFromDB, phoneNoFromDB));
                                        checkFirstTimeUser(phoneNoFromDB);
                                        user.setPassword(userEnteredPassword);
                                        saveUser();
                                        dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseLoginBefore()).getRef().setValue("TRUE");
                                        dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getGetFirebaseEmailVerified()).getRef().setValue("TRUE");
                                        dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebasePassword()).getRef().setValue(userEnteredPassword);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getGetFirebaseEmailVerified()).getValue(String.class).matches("TRUE")){
                                        // if the user changed the email
                                        phone.setError(null);
                                        phone.setErrorEnabled(false);
                                        Log.d(TAG, "login success  " + userEnteredPhone);
                                        String nameFromDB = dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseName()).getValue(String.class);
                                        String phoneNoFromDB = dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebasePhone()).getValue(String.class);

                                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                        intent.putExtra("Phone number", phoneNoFromDB);
                                        // save user phone number upon loggin
                                        user.copy(new User(nameFromDB, emailFromDB, phoneNoFromDB));
                                        user.setPassword(userEnteredPassword);
                                        saveUser();
                                        dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebaseLoginBefore()).getRef().setValue("TRUE");
                                        dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getGetFirebaseEmailVerified()).getRef().setValue("TRUE");
                                        dataSnapshot.child(userEnteredPhone).child(FirebaseStrings.getFirebasePassword()).getRef().setValue(userEnteredPassword);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Please Verify Your Email Address", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                else {
                                    Log.d(TAG, "password not equal " + userEnteredPhone);

                                    // progressBar.setVisibility(View.GONE);
                                    password.setError(" Incorrect Password");
                                    password.requestFocus();

                                }
                            }
                        });


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


    // saving local user information in shared preference file
    public static void saveUser(){
        clearUser();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(userPhone, user.getPhone());
        editor.putString(userPassword, user.getPassword());
        editor.commit();
    }

    // clear user information in shared preference file
    public static void clearUser(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(userPhone, "");
        editor.putString(userPassword, "");
        editor.commit();


    }

    private static boolean getUserExist(){return userExist;}

    private static void setUserExist(boolean val){ userExist = val;}


}