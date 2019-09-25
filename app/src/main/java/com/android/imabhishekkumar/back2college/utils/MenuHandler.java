package com.android.imabhishekkumar.back2college.utils;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.widget.WidgetProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuHandler implements PopupMenu.OnMenuItemClickListener {
    private DocumentReference db;
    private int position;
    private String parentUId;

    public MenuHandler(int positon, String parentUId) {
        this.position = positon;
        this.parentUId = parentUId;
    }


    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete:
                db = FirebaseFirestore.getInstance().collection("posts").document(parentUId);
                db.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                break;
            case R.id.pin:
                break;

            default:
        }
        return false;
    }
}
