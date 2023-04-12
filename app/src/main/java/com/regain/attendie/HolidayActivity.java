package com.regain.attendie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HolidayActivity extends AppCompatActivity {
ImageView holiday_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);
        holiday_image=findViewById(R.id.holiday_image);
        Toast.makeText(this,"Please wait...while image is loading",Toast.LENGTH_LONG).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Double Tap to zoom");
        Glide.with(this).load("https://webserviceuser.github.io/webservices/holiday_image.png").into(holiday_image);
        loadAds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    private void loadAds(){
//        AdRequest adRequest=new AdRequest.Builder().build();
//        adview3.loadAd(adRequest);
    }
}