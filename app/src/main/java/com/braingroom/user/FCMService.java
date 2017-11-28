package com.braingroom.user;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.braingroom.user.model.QRCode.PostDetail;
import com.braingroom.user.model.dto.FilterData;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import static com.braingroom.user.utils.CommonUtils.sendCustomEvent;

public class FCMService extends FirebaseMessagingService {

    public static final String TAG = "FCMService";
    protected Tracker mTracker;
    protected FirebaseAnalytics mFirebaseAnalytics;
    protected Random random = new Random();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (mTracker == null)
            mTracker = UserApplication.getInstance().getDefaultTracker();
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        UserApplication.getInstance().newNotificationBus.onNext(true);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.toString());

        if (remoteMessage.getData() != null)
            Log.d(TAG, "Payload:" + remoteMessage.getData());

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
        data.putBoolean(Constants.pushNotification, true);
        data.putString(Constants.pushNotification, mapToJson);
        int number = (random).nextInt(Integer.MAX_VALUE);
        Log.d(TAG, "sendNotification: " + number);
        intent = new Intent(this, Splash.class);
        intent.putExtra(Constants.pushNotification, data);
        intent.setAction(number+"");
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if ("text".equalsIgnoreCase(notificationType))
            notificationBuilder = createBigTextStyleNotification(title, shortDescription, detailDescription, pendingIntent);
        else if ("image".equalsIgnoreCase(notificationType))
            notificationBuilder = createBigPictureStyleNotification(title, getBitmapfromUrl(imageUrl), shortDescription, pendingIntent);
        else notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_64px)
                    .setLargeIcon(getBitmapFromResource(R.mipmap.ic_launcher))
                    .setContentTitle(title1 == null ? "" : title1)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.notify(number /* ID of notification */, notificationBuilder.build());
        sendCustomEvent(this, "Notification Received", notificationId, shortDescription);
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {

        HttpURLConnection connection = null;
        try {

            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public Bitmap getBitmapFromResource(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }


    private NotificationCompat.Builder createBigTextStyleNotification(String title, String shortDescription, String detailDescription, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this);
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

    private NotificationCompat.Builder createBigPictureStyleNotification(String title, Bitmap image, String shortDescription, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(builder);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (image == null)
            return new NotificationCompat.Builder(this)
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
