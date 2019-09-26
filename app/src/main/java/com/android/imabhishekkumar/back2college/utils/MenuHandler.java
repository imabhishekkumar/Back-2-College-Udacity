package com.android.imabhishekkumar.back2college.utils;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.model.ModelPost;
import com.android.imabhishekkumar.back2college.widget.WidgetProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;

public class MenuHandler implements PopupMenu.OnMenuItemClickListener {
    private DocumentReference db;
    private int position;
    private String parentUId;
    private  Context context;
    private  ModelPost modelPost;
    public MenuHandler(int positon, String parentUId, Context context, ModelPost modelPost) {
        this.position = positon;
        this.parentUId = parentUId;
        this.context = context;
        this.modelPost = modelPost;
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
                                Toast.makeText(context,"Successfully deleted!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Failed to delete.",Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.pin:
                SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("name", modelPost.getName());
                editor.putString("details", modelPost.getDetails());
                editor.putString("avatar", modelPost.getAvatar());
                editor.putString("image", modelPost.getMultimediaURL());
                editor.apply();
                Toast.makeText(context,"Place a widget to the homescreen to see this post.",Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }
}
