package com.android.imabhishekkumar.back2college.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.widget.RemoteViews;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.model.ModelPost;

import java.util.List;

public class WidgetProvider extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String userName, String avatar, String details,String image) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_post);

            views.setTextViewText(R.id.widget_username,userName);
            views.setTextViewText(R.id.widget_post_description,details);
            views.setImageViewUri(R.id.widget_avatar, Uri.parse(avatar));
            views.setImageViewUri(R.id.widget_image,Uri.parse(image));


        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            ModelPost modelPost = new ModelPost();
            updateAppWidget(context, appWidgetManager, appWidgetId, modelPost.getName(), modelPost.getAvatar(),modelPost.getDetails(),modelPost.getMultimediaURL());
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
