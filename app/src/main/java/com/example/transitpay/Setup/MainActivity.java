package com.example.transitpay.Setup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.transitpay.Authenticate.LoginActivity;
import com.example.transitpay.R;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(i);

                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
    
}