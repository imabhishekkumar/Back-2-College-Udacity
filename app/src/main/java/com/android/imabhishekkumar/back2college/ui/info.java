package com.android.imabhishekkumar.back2college.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.imabhishekkumar.back2college.R;

public class info extends AppCompatActivity {
TextView usernameTV;
ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        usernameTV= findViewById(R.id.info_username);
        backBtn = findViewById(R.id.back_Button);
        Intent intent= new Intent();
        usernameTV.setText(intent.getStringExtra("name"));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(info.this,Home.class));
                finish();
            }
        });
    }
}
