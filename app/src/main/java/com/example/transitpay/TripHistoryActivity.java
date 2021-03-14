package com.example.transitpay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TripHistoryActivity extends AppCompatActivity {
    protected TextView triphistoryTextView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_history_activity);
        setupUI();
    }
    private void setupUI(){
        triphistoryTextView=findViewById(R.id.triphistorytextView);
    }
}
