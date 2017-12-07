package com.braingroom.user.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by agrahari on 10/04/17.
 */

public class CommonUtils {

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        if (html == null) html = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }

    }


    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min)) + min;
    }

    public static void sendCustomEvent(String category, String action, String label) {
        category = category == null ? "" : category;
        action = action == null ? "" : action;
        label = label == null ? "" : label;
        UserApplication.getInstance().getDefaultTracker().send(new EventBuilder()
                .setCategory(category)
                .setAction(action).setLabel(label)
                .build());
        GoogleAnalytics.getInstance(UserApplication.getInstance()).dispatchLocalHits();
    }

    public static void sendCustomEvent(Context context, String category, String action, String label) {
        category = category == null ? "category" : category;
        action = action == null ? "action" : action;
        label = label == null ? "label" : label;
        Tracker mTracker = init(context);
        if (mTracker != null) {
            mTracker.send(new EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .build());
            GoogleAnalytics.getInstance(context).dispatchLocalHits();
        }

    }
    public static String getHumanDate(String timeStamp) {
        if (timeStamp == null)
            return "";
        long time = Long.valueOf(timeStamp) * 1000;
        try {
            java.text.DateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getHumanDateSmall(String timeStamp) {
        if (timeStamp == null)
            return "";
        long time = Long.valueOf(timeStamp) * 1000;
        try {
            java.text.DateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static Tracker init(Context ctx) {
        try {
            return GoogleAnalytics.getInstance(ctx).newTracker(R.xml.global_tracker);
        } catch (Exception e) {
        }
        return null;
    }

    public static HashMap<String, Integer> getSelectedDataMap(HashMap<String, Pair<Integer, String>> selectedDataMap) {
        HashMap<String, Integer> selectedData = new HashMap<>();
        Iterator it;
        if (selectedDataMap != null) {
            for (Map.Entry<String, Pair<Integer, String>> data : selectedDataMap.entrySet()) {
                selectedData.put(data.getKey(), data.getValue().first);
            }
        }
        return selectedData;
    }

}
