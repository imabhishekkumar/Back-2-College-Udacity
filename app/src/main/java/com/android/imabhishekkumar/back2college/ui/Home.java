package com.android.imabhishekkumar.back2college.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.imabhishekkumar.back2college.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.android.imabhishekkumar.back2college.ui.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.aviran.cookiebar2.CookieBar;

public class Home extends AppCompatActivity implements home_all.OnFragmentInteractionListener, home_department.OnFragmentInteractionListener, home_profile.OnFragmentInteractionListener {

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        mAuth = FirebaseAuth.getInstance();
        fab = findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.homeCoordinatorLayout);
        user = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final FloatingActionButton fab = findViewById(R.id.fab);
        documentReference = firebaseFirestore.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String isVerified = document.getString("verified");
                        if (isVerified.equals("true")) {
                            fab.show();
                        }
                    }
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AddPost.class));
            }
        });
        if(!isConnectionAvailable()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection available.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    public boolean isConnectionAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            startActivity(new Intent(Home.this, RegisterActivity.class));
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

