package com.android.imabhishekkumar.back2college.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.android.imabhishekkumar.back2college.R;


public class AppIntro extends com.github.paolorotolo.appintro.AppIntro {
    private Boolean firstTime = null;
    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();
            }

        }
        return firstTime;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isFirstTime()){
            addSlide(IntroSlide.newInstance(R.layout.fragment_first));
            addSlide(IntroSlide.newInstance(R.layout.fragment_second));
            askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            setBarColor(Color.parseColor("#00BBD3"));
            showSkipButton(false);

        }
        else{
            Intent first_time=new Intent(AppIntro.this, RegisterActivity.class);
            startActivity(first_time);
        }
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent skip=new Intent(AppIntro.this, RegisterActivity.class);
        startActivity(skip);
        finish();
    }
}
