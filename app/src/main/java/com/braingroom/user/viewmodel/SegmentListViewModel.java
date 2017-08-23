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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by godara on 09/08/17.
 */

public class SegmentListViewModel extends ViewModel {

    public final GridViewModel gridViewModel;


    public SegmentListViewModel(@NonNull final Navigator navigator, @NonNull final HashMap<String, Integer> categoryMap) {
        gridViewModel = new GridViewModel(navigator, GridViewModel.SEGMENT, categoryMap);
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                gridViewModel.retry();
                connectivityViewmodel.isConnected.set(true);
                Log.d(TAG, "run internet: " + connectivityViewmodel.isConnected.get());
            }
        });
    }


}
