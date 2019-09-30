package com.android.imabhishekkumar.back2college.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.imabhishekkumar.back2college.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoView extends AppCompatActivity {
    @BindView(R.id.photoView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String photoURL = intent.getStringExtra("imageUrl");
        Glide.with(getApplicationContext())
                .load(photoURL)
                .placeholder(R.drawable.loading)
                .into(imageView);

    }
}
