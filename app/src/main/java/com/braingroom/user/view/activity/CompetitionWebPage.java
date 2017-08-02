package com.braingroom.user.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;

import static com.braingroom.user.R.id.webview;

/**
 * Created by godara on 31/07/17.
 */

public class CompetitionWebPage extends BaseActivity {
    WebView webView;

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        webView = (WebView) findViewById(webview);
        webView.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });


        webView.loadUrl("https://www.braingroom.com/competition");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }
}
