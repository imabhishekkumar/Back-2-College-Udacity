package com.android.imabhishekkumar.back2college.ui;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.android.imabhishekkumar.back2college.R;

public class splashScreen extends AppCompatActivity {
private static int SPLASH_DISPLAY_LENGTH= 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(splashScreen.this, RegisterActivity.class);
                splashScreen.this.startActivity(mainIntent);
                splashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
