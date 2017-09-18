package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.SegmentListActivity;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by godara on 11/08/17.
 */

public class ConnectSectionListViewModel extends ViewModel {
    public final Navigator navigator;

    public final GridViewModel gridViewModel;
    public final Action hideThisPage;

    public ConnectSectionListViewModel(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final Navigator navigator) {
        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        this.navigator = navigator;
        gridViewModel = new GridViewModel(navigator, GridViewModel.CONNECT, null);

        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                gridViewModel.retry();
                connectivityViewmodel.isConnected.set(true);
                Log.d(TAG, "run internet: " + connectivityViewmodel.isConnected.get());
            }
        });

        hideThisPage = new Action() {
            @Override
            public void run() throws Exception {
                editor.putBoolean(Constants.HIDE_CONNECT_SECTIONS, true).commit();
            }
        };
    }

    @Override
    public void onResume() {
        connectivityViewmodel.onResume();
    }
}

