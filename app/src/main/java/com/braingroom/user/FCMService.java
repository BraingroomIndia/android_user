package com.braingroom.user;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.PostDetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    public static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("postId"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            sendNotification(remoteMessage);
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Intent intent;
        Bundle data = new Bundle();
        String postId=remoteMessage.getData().get("postId");
        String classId =remoteMessage.getData().get("classId");
        String title =remoteMessage.getNotification().getTitle();
        String messageBody= remoteMessage.getNotification().getBody();

        if (postId != null) {
            intent = new Intent(this, PostDetailActivity.class);
            data.putString("postId",postId);

        }
        else if(classId!=null) {
            intent = new Intent(this, ClassDetailActivity.class);
            data.putString("id",classId);

        }
        else {
            intent = new Intent(this,HomeActivity.class);
        }
        intent.putExtra("classData", data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title == null ? "" : title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
