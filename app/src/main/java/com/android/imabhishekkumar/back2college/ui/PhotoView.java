package com.android.imabhishekkumar.back2college.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.utils.AsyncHandler;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoView extends AppCompatActivity {
    @BindView(R.id.photoView)
    ImageView imageView;
    @BindView(R.id.photosaveBtn)
    Button saveBtn;

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

        saveBtn.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),R.string.will_be_saved,Toast.LENGTH_SHORT).show();
            AsyncHandler asyncTask = new AsyncHandler();
            asyncTask.execute(photoURL);
        });

    }
}
