package com.example.transitpay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

public class CitylinesActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylines_activity);
        setupUI();
    }
    private void setupUI(){
        PhotoView photoView = (PhotoView)
                findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.mtlmetro);
    }
}
