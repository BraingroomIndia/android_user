package com.braingroom.user.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;

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
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
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

    public static Tracker init(Context ctx) {
        try {
            return GoogleAnalytics.getInstance(ctx).newTracker(R.xml.global_tracker);
        } catch (Exception e) {
        }
        return null;
    }

}
