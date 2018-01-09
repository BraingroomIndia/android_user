package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.SegmentListActivity;
import com.braingroom.user.view.adapters.CustomGridLayoutManger;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

/**
 * Created by godara on 09/08/17.
 */

public class SegmentListViewModel extends ViewModel {

    public final GridViewModel gridViewModel;


    public SegmentListViewModel(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final Navigator navigator, @NonNull final HashMap<String, Integer> categoryMap, CustomGridLayoutManger layout) {
        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        if (!categoryMap.isEmpty())
            setScreenName(categoryMap.keySet().iterator().next());
        gridViewModel = new GridViewModel(navigator, GridViewModel.SEGMENT, categoryMap,layout);
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                gridViewModel.retry();
                connectivityViewmodel.isConnected.set(true);
                Timber.tag(TAG).d( "run internet: " + connectivityViewmodel.isConnected.get());
            }
        });
    }

    @Override
    public void onResume() {
        connectivityViewmodel.onResume();
    }


}
