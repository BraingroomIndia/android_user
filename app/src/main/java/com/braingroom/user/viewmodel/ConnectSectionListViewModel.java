package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

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

    public ConnectSectionListViewModel(@NonNull final Navigator navigator) {
        this.navigator = navigator;

        Observable<List<ViewModel>> connectSectionList = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getCategory().map(new Function<CategoryResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                        List<CategoryResp.Snippet> snippetList = categoryResp.getData();
                        final List<ViewModel> results = new ArrayList<>();
                        for (final CategoryResp.Snippet snippet : snippetList) {
                            results.add(new IconTextItemViewModel(snippet.getCategoryImage(), snippet.getCategoryName(),
                                    new MyConsumer<IconTextItemViewModel>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                            if (!snippet.getId().equals("-1")) {
                                                Bundle data = new Bundle();
                                                ConnectFilterData connectFilterData = new ConnectFilterData();
                                                connectFilterData.setMajorCateg(ConnectHomeActivity.LEARNER_FORUM);
                                                connectFilterData.setMinorCateg(ConnectHomeActivity.TIPS_TRICKS);
                                                data.putSerializable("connectFilterData", connectFilterData);
                                                navigator.navigateActivity(ConnectHomeActivity.class, data);
                                            }
                                        }
                                    }));
                        }
                        return results;
                    }
                });
            }
        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return new ArrayList<>();
            }
        });
        gridViewModel = new GridViewModel(connectSectionList, "");

        hideThisPage = new Action() {
            @Override
            public void run() throws Exception {
                editor.putBoolean(Constants.HIDE_CONNECT_SECTIONS, true).commit();
            }
        };
    }
}

