package com.example.transitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";

    TextView profile;
    Button saveButton;
    Button deleteButton;
    EditText name;
    EditText phone;
    EditText email;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // hook
        profile = findViewById(R.id.userProfileTxt);
        saveButton = findViewById(R.id.saveBn);
        deleteButton = findViewById(R.id.deleteBn);
        name = findViewById(R.id.nameTxt);
        phone = findViewById(R.id.phoneTxt);
        email = findViewById(R.id.emailTxt);

        displayProfile();

        // disable editing
        phone.setInputType(InputType.TYPE_NULL);

        setSaveButton();

        setDeleteButton();

    }

    // we can use security questions
    private void setDeleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText deleteAccount =  new EditText(v.getContext());
                deleteAccount.setInputType(0x00000002);
                AlertDialog.Builder deleteAccountDialog = new AlertDialog.Builder(v.getContext());
                deleteAccountDialog.setTitle("Delete account?");
                deleteAccountDialog.setMessage("Enter your phone number to confirm");
                deleteAccountDialog.setView(deleteAccount);

                deleteAccountDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //extract the phone to confirm the user.
                        String phoneStr = deleteAccount.getText().toString().trim();
                        if (LoginActivity.getUser().getPhone().matches(phoneStr)){
                            Toast.makeText(ProfileActivity.this, "Your account is deleted", Toast.LENGTH_SHORT).show();
                            // delete shared preference
                            LoginActivity.clearUser();
                            // delete local user obj
                            LoginActivity.getUser().copy(new User());
                            displayProfile();
                            // delete real time database
                            reference = FirebaseDatabase.getInstance().getReference("user").child(phoneStr);
                            reference.removeValue();
                            // delete authentication
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(ProfileActivity.this, "Failed to delete phone number miss matched", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                deleteAccountDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                deleteAccountDialog.create().show();
            }
        });
    }

    private void setSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // updated data
                final String nameStr = isEmpty(name);
                final String phoneStr = isEmpty(phone);
                final String emailStr = isEmpty(email);

                // check which fields are updated
                String [] newInfo = {nameStr,phoneStr, emailStr};
                boolean [] result = isUpdated(newInfo);

                int countUpdate = 0; // number of updated field
                int countEmpty = 0; // number of the empty field

                for(int i =0 ; i < result.length ; i++){
                    if (result[i]){
                        countUpdate++;
                    }
                    if(newInfo[i].matches("")){
                        countEmpty++;
                    }
                }

                // update is performed only if field has at least new value and none of fields are emptied
                if (countUpdate > 0 && countEmpty == 0){


                    String currentPhone = LoginActivity.getUser().getPhone();

                    // update database
                    reference = FirebaseDatabase.getInstance().getReference("user");
                    Query checkUser = reference.orderByChild("phone").equalTo(currentPhone);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d(TAG, "found user: " + phoneStr);

                                // check if the new email is taken by other user
                                if(isEmailChange(emailStr) && isEmailValid(email)){
                                    Query checkUserEmail = reference.orderByChild("email").equalTo(emailStr);
                                    checkUserEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Log.d(TAG, " Email is taken by another user " );

                                                //progressBar.setVisibility(View.GONE);
                                                email.setError("Email entered is user by another account \n" +
                                                        "delete existing account and try again");
                                                email.requestFocus();
                                                Toast.makeText(ProfileActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();

                                            } else{
                                                Log.d(TAG, " Updated successfully " );
                                                // accept updating new email address
                                                // delete old value from real time database and set the new value
                                                dataSnapshot.child(currentPhone).child("name").getRef().removeValue();
                                                dataSnapshot.child(currentPhone).child("name").getRef().setValue(nameStr);
                                                dataSnapshot.child(currentPhone).child("email").getRef().removeValue();
                                                dataSnapshot.child(currentPhone).child("email").getRef().setValue(emailStr);

                                                // update email in authentication
                                                //FirebaseAuth.getInstance().getCurrentUser().updateEmail(emailStr);
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                // get auth credential from the user for re authentication
                                                AuthCredential credential = EmailAuthProvider.getCredential(LoginActivity.getUser().getEmail(), LoginActivity.getUser().getPassword()); // current user credential
                                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d(TAG,"User reauthenticated");
                                                        // change the address
                                                        user.updateEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Log.d(TAG, "user email updated ");
                                                                }
                                                            }
                                                        });
                                                    }
                                                });


                                                // Update local user obj
                                                LoginActivity.getUser().setName(nameStr);
                                                LoginActivity.getUser().setPhone(phoneStr);
                                                LoginActivity.getUser().setEmail(emailStr);
                                                // update shared preference
                                                LoginActivity.saveUser();
                                                Toast.makeText(ProfileActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } else if(isNameChange(nameStr)){
                                    // if Email is not updated but name is updated
                                    dataSnapshot.child(currentPhone).child("name").getRef().removeValue();
                                    dataSnapshot.child(currentPhone).child("name").getRef().setValue(nameStr);

                                    // Update local user obj
                                    LoginActivity.getUser().setName(nameStr);
                                    // update shared preference
                                    LoginActivity.saveUser();
                                    // message to user only email is valid
                                    Toast.makeText(ProfileActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(ProfileActivity.this, "no changes", Toast.LENGTH_SHORT).show();

                                }



                            } else {
                                Log.d(TAG, "no snapshot " + phoneStr);

                                //progressBar.setVisibility(View.GONE);
                                phone.setError("No such User exist");
                                phone.requestFocus();
                                Toast.makeText(ProfileActivity.this, "No such User exist", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // do nothing.
                        }

                    });

                }else if(countUpdate <= 0){
                    Toast.makeText(ProfileActivity.this, "No updates", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProfileActivity.this, "empty fields found", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean isNameChange(String nameStr){
        return !LoginActivity.getUser().getName().matches(nameStr);
    }

    private boolean isEmailChange(String emailStr) {
        return !LoginActivity.getUser().getEmail().matches(emailStr);
    }

    private Boolean isEmailValid(EditText email){
        String emailStr = email.getText().toString().trim();
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

    // check the user updated the text box
    // if the user updated the info result is set to true
    private boolean [] isUpdated(String[] newInfo) {
        boolean result [] = {false,false,false};
        for (int i = 0; i < newInfo.length; i++){
            if (i == 0){
                // check name
                result[i] = (LoginActivity.getUser().getName().matches(newInfo[i]))? false:true;
            }else if(i == 1){
                // check email
                result[i] = (LoginActivity.getUser().getEmail().matches(newInfo[i]))? false:true;
            }else if(i == 2){
                // check phone
                result[i] = (LoginActivity.getUser().getPhone().matches(newInfo[i]))? false:true;
            }
        }
        return result;
    }

    // check if the field is empty
    // if the field is empty original value is preserved
    private  String isEmpty(EditText element){
        String str = element.getText().toString().trim();
        if (TextUtils.isEmpty(str)){
            element.setError("field cannot be empty");
            element.requestFocus();
            str =""; // return empty string
        }
        return str;
    }



    protected void displayProfile(){
        name.setText(LoginActivity.getUser().getName());
        phone.setText(LoginActivity.getUser().getPhone());
        email.setText(LoginActivity.getUser().getEmail());

    }

    // go back to main activity
    private void goToMainMenuActivity(){
        Intent intent= new Intent(ProfileActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set the menu icon of the page with layout xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }


    // Log out from the session when user click LOGOUT button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final Intent[] intent = {null}; // value of the intent is depends on the user selected button option
        switch(item.getItemId()){
            case R.id.Logout:
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
                            intent[0] = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent[0]);
                            finish();


                        }else{
                            Toast.makeText(ProfileActivity.
                                            this, "Phone number is not linked to an account",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}