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
import com.braingroom.user.view.activity.MessagesThreadActivity;
import com.braingroom.user.view.activity.PostDetailActivity;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Random;

public class FCMService extends FirebaseMessagingService {

    public static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        UserApplication.getInstance().newNotificationBus.onNext(true);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("post_id"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());

        }
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Intent intent;
        Bundle data = new Bundle();
        String notificationId = remoteMessage.getData().get("notification_id");
        String postId=remoteMessage.getData().get("post_id");
        String classId =remoteMessage.getData().get("class_id");
        String messageSenderId = remoteMessage.getData().get("sender_id");
        String messageSenderName = remoteMessage.getData().get("sender_name");
        String title =remoteMessage.getData().get("type");
        String messageBody= remoteMessage.getData().get("message");
        data.putString("notification_id",notificationId);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);



        if (postId != null) {
            intent = new Intent(this, PostDetailActivity.class);
            data.putString("postId",postId);

        }
        else if(classId!=null) {
            intent = new Intent(this, ClassDetailActivity.class);
            data.putString("id",classId);
            data.putString("origin", ClassListViewModel1.ORIGIN_HOME);

        }
        else if (messageSenderId!=null){
            intent= new Intent(this, MessagesThreadActivity.class);
            data.putString("sender_id",messageSenderId);
            data.putString("sender_name",messageSenderName);
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

        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }

}
