package com.android.imabhishekkumar.back2college.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;
import com.android.imabhishekkumar.back2college.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;


public class WidgetProvider extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String name, String details, String avatar, String image) {
        RemoteViews views;
        if (image!=null) {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_post);
            views.setImageViewUri(R.id.widget_image, Uri.parse(image));
            views.setTextViewText(R.id.widget_username, name);
            views.setTextViewText(R.id.widget_post_description, details);
            views.setImageViewUri(R.id.widget_avatar, Uri.parse(avatar));

            AppWidgetTarget awtAvatar = new AppWidgetTarget(context, R.id.widget_avatar, views, appWidgetId) {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    super.onResourceReady(resource, transition);
                }
            };


            Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(avatar)
                    .into(awtAvatar);

            AppWidgetTarget awtImage = new AppWidgetTarget(context, R.id.widget_image, views, appWidgetId) {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    super.onResourceReady(resource, transition);
                }
            };


            Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(image)
                    .override(480, 342)
                    .into(awtImage);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_no_img);
            views.setTextViewText(R.id.widget_ni_username, name);
            views.setTextViewText(R.id.widget_ni_post_description, details);
            views.setImageViewUri(R.id.widget_ni_avatar, Uri.parse(avatar));

            AppWidgetTarget awtAvatar = new AppWidgetTarget(context, R.id.widget_ni_avatar, views, appWidgetId) {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    super.onResourceReady(resource, transition);
                }
            };


            Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(avatar)
                    .into(awtAvatar);
        }


        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String name = pref.getString("name", null);
        String details = pref.getString("details", null);
        String avatar = pref.getString("avatar", null);
        String image = pref.getString("image", null);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, name, details, avatar, image);
        }


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
