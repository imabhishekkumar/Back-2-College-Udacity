package com.android.imabhishekkumar.back2college.service;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.imabhishekkumar.back2college.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class notification extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }
public void showNotification(String title, String message){
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"DeanNotification")
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.splash_logo)
            .setAutoCancel(true)
            .setContentText(message);
    NotificationManagerCompat manager =NotificationManagerCompat.from(this);
    manager.notify(999,builder.build());

}
}
