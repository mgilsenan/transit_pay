package com.example.transitpay;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.transitpay.LoginActivity.user;


public class ProfileFragment extends Fragment {
    private final String TAG = "ProfileFragment";
    //public static boolean upDateF = false;  // check if the user info is updated. if false in unsuccessful message is toasted
    View view;
    Button saveBtn;
    EditText name;
    EditText email;
    EditText phone;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Log.d(TAG,"profile fragment onCreateView");

        // hook UI
        saveBtn = (Button)view.findViewById(R.id.saveBtn);
        name = (EditText)view.findViewById(R.id.name);
        email = (EditText)view.findViewById(R.id.email);
        phone = (EditText)view.findViewById(R.id.phone);
        reference = FirebaseDatabase.getInstance().getReference("user");

        // if text is not empty save and update the basic user info
        // and take them to the main menu
        // firebase & local user obj needs to be updated
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"profile fragment onClick");

                // user's info in firebase is updated
                Query checkUser = reference.orderByChild("phone").equalTo(LoginActivity.getUser().getPhone());

                // search the user in database
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Log.d(TAG, "profile fragment user found");

                            if (name.toString().isEmpty() || email.toString().isEmpty() || phone.toString().isEmpty()){
                                Log.d(TAG, "profile fragment update failed");

                                // upDateF = false;
                                Toast.makeText(getActivity(),"Update failed make sure to filled the all required fields", Toast.LENGTH_LONG).show();

                            }else{
                                Log.d(TAG, "profile fragment update success");

                                // upDateF = true;
                                // update the database info first
                                reference.child(LoginActivity.getUser().getPhone()).child("name").setValue(name);
                                reference.child(LoginActivity.getUser().getPhone()).child("email").setValue(email);
                                reference.child(LoginActivity.getUser().getPhone()).child("phone").setValue(phone);




                                // user obj created when login is updated
                                LoginActivity.getUser().setName(name.toString());
                                LoginActivity.getUser().setEmail(email.toString());
                                LoginActivity.getUser().setPhone(phone.toString());
                            }

                        } else {// should never enter this state
                            Log.d(TAG, "profile fragment user not found");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // do nothing
                    }
                });



            }
        });
                return view;
    }
   // public static boolean getupDateF(){return upDateF;}
}