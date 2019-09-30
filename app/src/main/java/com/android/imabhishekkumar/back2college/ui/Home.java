package com.android.imabhishekkumar.back2college.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.imabhishekkumar.back2college.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.imabhishekkumar.back2college.ui.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.aviran.cookiebar2.CookieBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity implements home_all.OnFragmentInteractionListener, home_department.OnFragmentInteractionListener, home_profile.OnFragmentInteractionListener {

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.homeCoordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        mAuth = FirebaseAuth.getInstance();
        toolbar.inflateMenu(R.menu.main_menu);
        user = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String isVerified = document.getString("verified");
                    if (isVerified.equals("true")) {
                        fab.show();
                    }
                }
            }
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.signout) {
                AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(this,
                        task -> {
                            mAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                            finish();
                        });

                return true;
            }
            return true;
        });
        fab.setOnClickListener(view -> startActivity(new Intent(Home.this, AddPost.class)));
        if (!isConnectionAvailable()) {
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

