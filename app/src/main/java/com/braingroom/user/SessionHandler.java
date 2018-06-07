package com.braingroom.user;

import android.util.Log;
import android.view.View;

import com.braingroom.user.view.MessageHelper;

import javax.inject.Inject;

/**
 * Created by android on 29/05/18.
 */

public class SessionHandler {
    private final String TAG = "SessionHandler";
    MessageHelper messageHelper;

    public void checkFullSession(View view, boolean isVisible) {
        messageHelper.show("Full session is checked");
        view.animate();
        Log.d("checked","full session");
        Log.d(TAG, isVisible ? "Fullsession is visible" : "Fullsession is not visible");
    }
}
