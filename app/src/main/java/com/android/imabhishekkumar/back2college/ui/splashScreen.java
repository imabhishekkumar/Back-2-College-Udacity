package com.android.imabhishekkumar.back2college.ui;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.imabhishekkumar.back2college.R;

public class splashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent mainIntent = new Intent(splashScreen.this, RegisterActivity.class);
        startActivity(mainIntent);
        finish();


    }
}
