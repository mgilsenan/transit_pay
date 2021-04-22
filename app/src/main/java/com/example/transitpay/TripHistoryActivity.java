package com.example.transitpay;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.transitpay.Authenticate.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TripHistoryActivity extends AppCompatActivity {
    FirebaseDatabase tDatabase;
    DatabaseReference dbTrip;
    DatabaseReference ref;
    private RecyclerView recyclerView;
    private TripHistoryAdapter adapter;
    private List<Trip> tripList;
    private ImageButton refreshButton;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_history_activity);
//        String phoneNumber = getIntent().getStringExtra("Phone number");
        String phoneNumber= LoginActivity.getUser().getPhone();
        Log.e("PHONE NUMBER", "onCreate: "+phoneNumber);
        tDatabase = FirebaseDatabase.getInstance();
        dbTrip=tDatabase.getReference("user");
        String path="user/";
        String path1=path.concat(phoneNumber+"/Trips");
        ref=FirebaseDatabase.getInstance().getReference(path1);
        tripList = new ArrayList<>();
        retrieveTrips();
        setupUI();
    }
    private void setupUI(){
        swipeRefreshLayout=findViewById(R.id.swipeRef);
        recyclerView = findViewById(R.id.trip_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripList = new ArrayList<>();
        adapter = new TripHistoryAdapter(this, tripList);
        recyclerView.setAdapter(adapter);

       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               retrieveTrips();
               adapter.notifyDataSetChanged();
               swipeRefreshLayout.setRefreshing(false);
           }
       });
//        refreshButton=findViewById(R.id.refreshButton);
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                retrieveTrips();
//            }
//        });

    }

    private void retrieveTrips(){

        Query trip = ref.limitToLast(10);
        trip.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tripList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Trip trip = snapshot.getValue(Trip.class);
                        tripList.add(trip);
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), " Trip history is not Available.\n" +
                                    " To see your trip details, take your first trip.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


