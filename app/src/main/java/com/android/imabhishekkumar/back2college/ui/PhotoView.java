package com.android.imabhishekkumar.back2college.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.imabhishekkumar.back2college.R;
import com.bumptech.glide.Glide;

public class PhotoView extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        imageView = findViewById(R.id.photoView);
        Intent intent = getIntent();
        String photoURL = intent.getStringExtra("imageUrl");
        Glide.with(getApplicationContext())
                .load(photoURL)
                .placeholder(R.drawable.loading)
                .into(imageView);

    }
}
