package com.example.transitpay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CitylinesActivity extends AppCompatActivity {
    protected TextView citylinesTextView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylines_activity);
        setupUI();
    }
    private void setupUI(){
        citylinesTextView=findViewById(R.id.citylinestextView);
    }
}
