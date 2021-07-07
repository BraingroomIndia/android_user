package com.braingroom.user.view;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.braingroom.user.R;

/*
 * Created by apple on 19/03/18.
 */

public class FullScreenVideoActivity extends AppCompatActivity {
    protected Bundle extras;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (getActionBar() != null)
            getActionBar().hide();

        if (getIntent().getExtras() != null)
            extras = getIntent().getExtras().getBundle("classData");
        setContentView(R.layout.activity_full_screen_video);
        VideoView fullscreenVideoLayout;

        fullscreenVideoLayout = findViewById(R.id.videoview);
        fullscreenVideoLayout.setVideoURI(getUri(getIntentString("video_url")));
        MediaController mediaController = new MediaController(fullscreenVideoLayout.getContext());
        fullscreenVideoLayout.setMediaController(mediaController);

        fullscreenVideoLayout.start();


    }


    public String getIntentString(String key) {
        if (extras != null) {
            return extras.getString(key);
        } else return null;

    }

    private Uri getUri(String url) {
        try {
            return Uri.parse(url);
        } catch (Exception e) {
            return Uri.EMPTY;
        }

    }

}

