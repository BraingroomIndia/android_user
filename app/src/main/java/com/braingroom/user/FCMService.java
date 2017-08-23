package com.braingroom.user;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.MessagesThreadActivity;
import com.braingroom.user.view.activity.PostDetailActivity;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

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
        String title = remoteMessage.getData().get("title");
        String shortDescription = remoteMessage.getData().get("short_description");
        String detailDescription = remoteMessage.getData().get("detail_description");
        String imageUrl = remoteMessage.getData().get("image");
        String notificationId = remoteMessage.getData().get("notification_id");
        String postId = remoteMessage.getData().get("post_id");
        String classId = remoteMessage.getData().get("class_id");
        String messageSenderId = remoteMessage.getData().get("sender_id");
        String messageSenderName = remoteMessage.getData().get("sender_name");
        data.putString("notification_id", notificationId);
        data.putBoolean("pushNotification", true);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


        if (postId != null) {
            intent = new Intent(this, PostDetailActivity.class);
            data.putString("postId", postId);

        } else if (classId != null) {
            intent = new Intent(this, ClassDetailActivity.class);
            data.putString("id", classId);
            data.putString("origin", ClassListViewModel1.ORIGIN_HOME);

        } else if (messageSenderId != null) {
            intent = new Intent(this, MessagesThreadActivity.class);
            data.putString("sender_id", messageSenderId);
            data.putString("sender_name", messageSenderName);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        intent.putExtra("classData", data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if (TextUtils.isEmpty(imageUrl))
            notificationBuilder = createBigTextStyleNotification(title, shortDescription, detailDescription, pendingIntent);
        else
            notificationBuilder = createBigPictureStyleNotification(title, imageUrl, shortDescription, pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private NotificationCompat.Builder createBigTextStyleNotification(String title, String shortDescription, String detailDescription, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        style.bigText(detailDescription).setBigContentTitle(title);
        builder.setContentTitle(title).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentText(shortDescription).
                setStyle(style).
                setAutoCancel(true).
                setSound(defaultSoundUri).
                setContentIntent(pendingIntent);
        ;
        return builder;
    }

    private NotificationCompat.Builder createBigPictureStyleNotification(String title, String imageUrl, String shortDescription, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(builder);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        style.setBigContentTitle(title).setSummaryText(shortDescription).bigPicture(getBitmapfromUrl(imageUrl));
        builder.setContentTitle(title).
                setContentText(shortDescription).
                setSmallIcon(R.mipmap.ic_launcher).
                setStyle(style).
                setAutoCancel(true).
                setSound(defaultSoundUri).
                setContentIntent(pendingIntent);
        return builder;
    }
}
