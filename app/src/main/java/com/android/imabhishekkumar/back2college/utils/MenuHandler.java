package com.android.imabhishekkumar.back2college.utils;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.model.ModelPost;
import com.android.imabhishekkumar.back2college.widget.WidgetProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.text.BreakIterator;

public class MenuHandler implements PopupMenu.OnMenuItemClickListener {
    private DocumentReference db;
    private int position;
    private String parentUId;
    private Context context;
    private ModelPost modelPost;

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
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                TextView title = new TextView(context);
                // Title Properties
                title.setText(R.string.confirmDelete);
                title.setPadding(8, 8, 8, 8);   // Set Position
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.BLACK);
                title.setTextSize(20);
                alertDialog.setCustomTitle(title);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db = FirebaseFirestore.getInstance().collection("posts").document(parentUId);
                        db.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                new Dialog(context);
                alertDialog.show();
                final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
                neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
                okBT.setPadding(50, 10, 10, 10);   // Set Position
                okBT.setTextColor(Color.BLACK);
                okBT.setLayoutParams(neutralBtnLP);

                final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
                negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
                cancelBT.setTextColor(Color.BLACK);
                cancelBT.setLayoutParams(negBtnLP);

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
                Toast.makeText(context, "Place a widget to the homescreen to see this post.", Toast.LENGTH_LONG).show();
                break;

            case R.id.share:
                final Intent shareIntent = new Intent();
                if (modelPost.getMultimediaURL() != null) {
                    Glide.with(context)
                            .asBitmap()
                            .load(modelPost.getMultimediaURL())
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    ImageUtils imageUtils = new ImageUtils();
                                    File file = imageUtils.storeImage(resource, context);
                                    Uri apkURI = FileProvider.getUriForFile(
                                            context,
                                            context.getApplicationContext()
                                                    .getPackageName() + ".provider", file);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, apkURI);
                                    shareIntent.setType("image/jpeg");
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, modelPost.getDetails());
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    context.startActivity(Intent.createChooser(shareIntent, "Choose an option"));
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });

                } else {
                    shareIntent.setType("text/plain");


                }
                break;
        }
        return false;
    }
}
