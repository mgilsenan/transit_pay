package com.example.transitpay;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {
    Context myContext;
    LayoutInflater layoutInflater;
    String[] slideTitle;
    String[] slideDes;
    public SliderAdapter(Context context){

        this.myContext=context;
        slideTitle= new String[]{"Get Started",
                "Choose Your Fare",
                "Track Your Trips details",
                "Find Out Whats Near By"
        };
        slideDes= new String[]{
                "Activate your metro card to go places",
                "Do not stand in line\n"+"Purchase a Fare with our App",
                "Browse your previous trips info.\n Find out Location, Date and Time of your recent"+
                " trips",
                "Plan your trip.\n" +
                        "Locate metro stations and other places of interest. "
        };

    }
    public int[] slideImages ={
            R.drawable.pay_logo,
            R.drawable.cart,
            R.drawable.gps_logo,
            R.drawable.trip_logo
    };

    @Override
    public int getCount() {
        return slideTitle.length;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== object;//(RelativeLayout)
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        layoutInflater=(LayoutInflater)myContext.getSystemService(myContext.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slide_layout,container, false);//

        ImageView image = view.findViewById(R.id.introImageView);
        TextView title=(TextView)view.findViewById(R.id.titletextView);
        TextView info=view.findViewById(R.id.infotextView);

        image.setImageResource(slideImages[position]);
        title.setText(slideTitle[position] );
        info.setText(slideDes[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
