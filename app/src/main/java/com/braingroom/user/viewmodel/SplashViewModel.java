package com.braingroom.user.viewmodel;

import android.databinding.ObservableInt;

import io.reactivex.functions.Action;

import com.braingroom.user.R;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CatalogueHomeActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.HomeActivity;

/**
 * Created by godara on 29/07/17.
 */

public class SplashViewModel extends ViewModel {
    public final ObservableInt background;
    public final Action onCalogueClicked, onFindClicked, onConnectClicked;

    public SplashViewModel(final Navigator navigator) {
        background = new ObservableInt(R.drawable.find_background);
        onFindClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(HomeActivity.class, null);
                navigator.finishActivity();
            }
        };
        onCalogueClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(CatalogueHomeActivity.class, null);
                navigator.finishActivity();
            }
        };
        onConnectClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(ConnectHomeActivity.class, null);
                navigator.finishActivity();
            }
        };
    }

}
