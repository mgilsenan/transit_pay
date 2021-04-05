package com.example.transitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";

    TextView profile;
    Button mainMenuButton;
    Button goBackButton;
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
        mainMenuButton = findViewById(R.id.saveBn);
        goBackButton = findViewById(R.id.goBackBn);
        name = findViewById(R.id.nameTxt);
        phone = findViewById(R.id.phoneTxt);
        email = findViewById(R.id.emailTxt);

        displayProfile();

        // disable editing  
        name.setInputType(InputType.TYPE_NULL);


        mainMenuButton.setOnClickListener(new View.OnClickListener() {
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
                    if(newInfo[i] == ""){
                        countEmpty++;
                    }
                }

                // update is performed only if field has at least new value and none of fields are emptied
                if (countUpdate > 0 && countEmpty == 0){

                    String currentPhone = LoginActivity.getUser().getPhone();

                    // update database
                    reference = FirebaseDatabase.getInstance().getReference("user");
                    Query checkUser = reference.orderByChild("phone").equalTo(LoginActivity.getUser().getPhone());

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d(TAG, "found user: " + phoneStr);

                                // look up the user account in the database
                                String phoneFromDB = dataSnapshot.child(currentPhone).child("phone").getValue(String.class);
                                // delete old data and set the new data
                                dataSnapshot.child(currentPhone).child("name").getRef().removeValue();
                                dataSnapshot.child(currentPhone).child("name").getRef().setValue(nameStr);
                                dataSnapshot.child(currentPhone).child("email").getRef().removeValue();
                                dataSnapshot.child(currentPhone).child("email").getRef().setValue(emailStr);

                                // to update the phone number
                                // delete current user from real time database,
                                dataSnapshot.child(currentPhone).child("phone").getRef().removeValue();
                                dataSnapshot.child(currentPhone).child("phone").getRef().setValue(phoneStr);

                                // update email in authentication
                                FirebaseAuth.getInstance().getCurrentUser().updateEmail(emailStr);

                                // Update local user obj
                                LoginActivity.getUser().setName(name.toString());
                                LoginActivity.getUser().setName(phone.toString());
                                LoginActivity.getUser().setName(email.toString());
                                Toast.makeText(ProfileActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();


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

                }

                //goToMainMenuActivity();
            }
        });

    }

    // check the user updated the text box
    // if the user updated the info result is set to true
    private boolean [] isUpdated(String[] newInfo) {
        boolean result [] = {false,false,false};
        for (int i = 0; i < newInfo.length; i++){
            if (i == 0){
                // check name
                result[i] = (LoginActivity.getUser().getName() == newInfo[i])? false:true;
            }else if(i == 1){
                // check email
                result[i] = (LoginActivity.getUser().getEmail() == newInfo[i])? false:true;
            }else if(i == 2){
                // check phone
                result[i] = (LoginActivity.getUser().getPhone() == newInfo[i])? false:true;
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
        Intent intent = null; // value of the intent is depends on the user selected button option
        switch(item.getItemId()){
            case R.id.Logout:
                Toast.makeText(this, "Logged out from account", Toast.LENGTH_LONG).show();
                // destroy local user obj and return to login activity
                LoginActivity.getUser().setPhone("");
                LoginActivity.getUser().setName("");
                LoginActivity.getUser().setEmail("");
                intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}