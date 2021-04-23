package com.example.transitpay.Setup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.transitpay.Authenticate.LoginActivity;
import com.example.transitpay.R;

public class GetStarted extends AppCompatActivity {
    Context myContext;
    private ViewPager slideViewPager;
    private LinearLayout DotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] dots;
    private Button backButton;
    private Button nextButton;
    private int currentPage;
   // private Button skipButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        slideViewPager=findViewById(R.id.slideViewPager);
        DotLayout=findViewById(R.id.dotsLinearLayout);
//        backButton=findViewById(R.id.backslideButton);
        nextButton=findViewById(R.id.nextslideButton);
        //skipButton=findViewById(R.id.skipButton);

        dots= new TextView[4];
        for (int i=0; i<dots.length; i++){
            dots[i]= new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(getResources().getColor(R.color.white));
            DotLayout.addView(dots[i]);
        }

        addDotsIndicator(0);
        sliderAdapter= new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);

        slideViewPager.addOnPageChangeListener(viewListener);

//        skipButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(GetStarted.this, MainMenuActivity.class);
//                startActivity(intent);
//            }
//        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(GetStarted.this, LoginActivity.class);
                startActivity(intent);
//                slideViewPager.setCurrentItem(currentPage+1);

            }
        });
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                slideViewPager.setCurrentItem(currentPage-1);
//
//            }
//        });

    }
    //function to add the number of dots needed
    public void addDotsIndicator(int position){
//        dots= new TextView[4];
//        for (int i=0; i<dots.length; i++){
//            dots[i]= new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(25);
//            dots[i].setTextColor(getResources().getColor(R.color.white));
//            DotLayout.addView(dots[i]);
//        }
        if(dots.length>0){
            dots[position].setTextColor(ContextCompat.getColor(this, R.color.jungleGreen));

        }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage=position;
//            if(position==0){
//                nextButton.setEnabled(true);
//                backButton.setEnabled(false);
//                backButton.setVisibility(View.INVISIBLE);
//
//            }else
                if(position==dots.length-1){
//                nextButton.setEnabled(true);
//                backButton.setVisibility(View.VISIBLE);

                nextButton.setText("Done");
            }
            else{
                nextButton.setText("Skip");
//                backButton.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
