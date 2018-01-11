package com.braingroom.user.utils;

import timber.log.Timber;

import android.util.Log;

import com.braingroom.user.UserApplication;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;

/*
 * Created by godara on 09/01/18.
 */

public class ProductionTree extends Timber.Tree {
    public ProductionTree(UserApplication app) {
        // Initialize Fabric with Crashlytics.
        Fabric.with(app, new Crashlytics(), new Answers());
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        // Log the message to Crashlytics, so we can see it in crash reports;

        // Log the exception in Crashlytics if we have one.
        if (t != null) {
            Crashlytics.logException(t);
        }

        // If this is an error or a warning, log it as a exception so we see it in Crashlytics.
        if (priority > Log.WARN) {
            Crashlytics.log(priority, tag, message);
            //Crashlytics.logException(new Throwable(message));
        }

        // Track INFO level logs as custom Answers events.
        if (priority == Log.INFO) {
            // Answers.getInstance().logCustom(new CustomEvent(message));
        }
    }
}
