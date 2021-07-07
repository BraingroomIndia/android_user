package com.braingroom.user;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.braingroom.user.model.QRCode.PostDetail;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.PostDetailActivity;
import com.braingroom.user.view.activity.Splash;
import com.braingroom.user.view.activity.MessageActivity;
import com.braingroom.user.view.activity.MessagesThreadActivity;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import timber.log.Timber;

import static com.braingroom.user.utils.CommonUtils.sendCustomEvent;

public class FCMService extends FirebaseMessagingService {
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";


    public static final String TAG = "FCMService";
    protected Tracker mTracker;
    protected FirebaseAnalytics mFirebaseAnalytics;
    protected Random random = new Random();
    int screenWidth;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (mTracker == null)
            mTracker = UserApplication.getInstance().getDefaultTracker();
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (screenWidth == 0)
            screenWidth = CommonUtils.getScreenWidth();
        Timber.tag(TAG).d("Payload:" + remoteMessage.getData());

        if (remoteMessage.getData() != null)
            sendNotification(remoteMessage);

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Gson objGson = new GsonBuilder().setPrettyPrinting().create();
        Intent intent;
        Bundle data = new Bundle();
        String notificationType = remoteMessage.getData().get("notify_type");

        String notificationId = remoteMessage.getData().get("notification_id");
        String title = null;
        String title1 = null;
        String messageBody = null;
        String shortDescription = null;
        String detailDescription = null;
        String imageUrl = null;
        if (notificationType == null) {
            notificationType = "";
            title1 = remoteMessage.getData().get("type");
            messageBody = remoteMessage.getData().get("message");
        } else if (notificationType.equalsIgnoreCase("text")) {
            title = remoteMessage.getData().get("title");
            shortDescription = remoteMessage.getData().get("short_description");
            detailDescription = remoteMessage.getData().get("detail_description");
        } else if (notificationType.equalsIgnoreCase("image")) {
            title = remoteMessage.getData().get("title");
            shortDescription = remoteMessage.getData().get("short_description");
            imageUrl = remoteMessage.getData().get("image");
        }
        String mapToJson = objGson.toJson(remoteMessage.getData());
        data.putString(Constants.pushNotification, mapToJson);
        int number = (random).nextInt(Integer.MAX_VALUE);
        Timber.tag(TAG).d("sendNotification: " + number);
        intent = new Intent(this, Splash.class);
        intent.putExtra(Constants.pushNotification, data);
        intent.setAction(number + "");
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if ("text".equalsIgnoreCase(notificationType))
            notificationBuilder = createBigTextStyleNotification(title, shortDescription, detailDescription, pendingIntent, notificationType);
        else if ("image".equalsIgnoreCase(notificationType))
            notificationBuilder = createBigPictureStyleNotification(title, getBitmapfromUrl(imageUrl), shortDescription, pendingIntent, notificationType);
        else notificationBuilder = new NotificationCompat.Builder(this, notificationType)
                    .setSmallIcon(R.drawable.ic_notifications_64px)
                    .setLargeIcon(getBitmapFromResource(R.mipmap.ic_launcher))
                    .setContentTitle(title1 == null ? "" : title1)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Default");
            //notificationChannel.enableLights(true);
            //notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            //notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        if (notificationManager != null) // Handle image related nonfiction separately
            notificationManager.notify(number /* ID of notification */, notificationBuilder.build());
        sendCustomEvent(this, "Notification Received", notificationId, shortDescription);
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            if (screenWidth != 0)
                return Picasso.with(this).load(imageUrl).resize(screenWidth, screenWidth / 2).get();
            else return Picasso.with(this).load(imageUrl).get();
        } catch (IOException e) {
            Timber.tag(TAG).e(e, "Image issue");

        }
        return null;
    }


    public Bitmap getBitmapFromResource(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }


    private NotificationCompat.Builder createBigTextStyleNotification(String title, String shortDescription, String detailDescription, PendingIntent pendingIntent, String notificationType) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        style.bigText(detailDescription).setBigContentTitle(title);

        builder.setContentTitle(title).
                setSmallIcon(R.drawable.ic_notifications_64px).
                setLargeIcon(getBitmapFromResource(R.mipmap.ic_launcher)).
                setColor(getResources().getColor(R.color.push_notification)).
                setContentText(shortDescription).
                setStyle(style).
                setAutoCancel(true).
                setSound(defaultSoundUri).
                setContentIntent(pendingIntent);

        return builder;
    }

    private NotificationCompat.Builder createBigPictureStyleNotification(String title, Bitmap image, String shortDescription, PendingIntent pendingIntent, String notificationType) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(builder);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (image == null)
            return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_64px)
                    .setLargeIcon(getBitmapFromResource(R.mipmap.ic_launcher))
                    .setContentTitle(title == null ? "" : title)
                    .setContentText(shortDescription)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        style.setBigContentTitle(title).setSummaryText(shortDescription).bigPicture(image);
        builder.setContentTitle(title).
                setContentText(shortDescription).
                setColor(getResources().getColor(R.color.push_notification)).
                setSmallIcon(R.drawable.ic_notifications_64px).
                setLargeIcon(getBitmapFromResource(R.mipmap.ic_launcher)).
                setStyle(style).
                setAutoCancel(true).
                setSound(defaultSoundUri).
                setContentIntent(pendingIntent);
        return builder;
    }


}
