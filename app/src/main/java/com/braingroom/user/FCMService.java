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
import android.support.annotation.ColorInt;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.MessageActivity;
import com.braingroom.user.view.activity.MessagesThreadActivity;
import com.braingroom.user.view.activity.PostDetailActivity;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class FCMService extends FirebaseMessagingService {

    public static final String TAG = "FCMService";
    protected Tracker mTracker;
    protected FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (mTracker == null)
            mTracker = UserApplication.getInstance().getDefaultTracker();
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        UserApplication.getInstance().newNotificationBus.onNext(true);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
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
        String notificationType = remoteMessage.getData().get("notify_type");
        String title = remoteMessage.getData().get("title");
        String shortDescription = remoteMessage.getData().get("short_description");
        String detailDescription = remoteMessage.getData().get("detail_description");
        String imageUrl = remoteMessage.getData().get("image");
        String notificationId = remoteMessage.getData().get("notification_id");
        String postId = remoteMessage.getData().get("post_id");
        String classId = remoteMessage.getData().get("class_id");
        String messageSenderId = remoteMessage.getData().get("sender_id");
        String messageSenderName = remoteMessage.getData().get("sender_name");
        String title1 = remoteMessage.getData().get("type");
        String messageBody = remoteMessage.getData().get("message");
        data.putString("notification_id", notificationId);
        data.putBoolean("pushNotification", true);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if (mTracker != null) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Notification Received")
                    .setAction(notificationId)
                    .setLabel(shortDescription)
                    .setNonInteraction(false)
                    .build());
        }


        if (postId != null) {
            intent = new Intent(this, PostDetailActivity.class);
            data.putString("postId", postId);

        } else if (classId != null) {
            intent = new Intent(this, ClassDetailActivity.class);
            data.putString("id", classId);
            data.putString("origin", ClassListViewModel1.ORIGIN_HOME);

        } else if (messageSenderId != null) {
            if ("0".equalsIgnoreCase(messageSenderId))
                intent = new Intent(this, MessageActivity.class);
            else
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
        if ("text".equalsIgnoreCase(notificationType))
            notificationBuilder = createBigTextStyleNotification(title, shortDescription, detailDescription, pendingIntent);
        else if ("image".equalsIgnoreCase(notificationType))
            notificationBuilder = createBigPictureStyleNotification(title, imageUrl, shortDescription, pendingIntent);
        else notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_64px)
                    .setContentTitle(title1 == null ? "" : title1)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

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
                setSmallIcon(R.drawable.ic_notifications_64px).
                setColor(getResources().getColor(R.color.push_notification)).
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
                setColor(getResources().getColor(R.color.push_notification)).
                setSmallIcon(R.drawable.ic_notifications_64px).
                setStyle(style).
                setAutoCancel(true).
                setSound(defaultSoundUri).
                setContentIntent(pendingIntent);
        return builder;
    }

}
