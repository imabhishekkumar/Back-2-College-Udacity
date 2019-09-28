package com.android.imabhishekkumar.back2college.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class splashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(splashScreen.this, AppIntro.class);
        startActivity(mainIntent);
        finish();

    }
}
