package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

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
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by godara on 09/08/17.
 */

public class SegmentListViewModel extends ViewModel {

    public final Observable<List<ViewModel>> segments;
    public final GridViewModel gridViewModel;
    private final String categoryName;
    private final String categoryId;

    public SegmentListViewModel(@NonNull final Navigator navigator, @NonNull final HashMap<String, Integer> categoryMap) {
        Map.Entry<String, Integer> entry = categoryMap.entrySet().iterator().next();
        categoryId = entry.getValue() + "";
        categoryName = entry.getKey();
        segments = FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getSegments(categoryId).map(new Function<SegmentResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(SegmentResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();

                        for (final SegmentResp.Snippet elem : resp.getData()) {
                            if (!elem.getId().equals("-1")) {
                                apiSuccessful = true;
                                results.add(new IconTextItemViewModel(elem.getSegmentImage(), elem.getSegmentName(),
                                        new MyConsumer<IconTextItemViewModel>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                                {
                                                    Bundle data = new Bundle();
                                                    FilterData filterData = new FilterData();
                                                    HashMap<String, Integer> segmentFilterMap = new HashMap<String, Integer>();
                                                    segmentFilterMap.put(elem.getSegmentName(), Integer.parseInt(elem.getId()));
                                                    filterData.setCategoryId(categoryId);
                                                    filterData.setSegmentId(elem.getId());
                                                    data.putSerializable("categoryMap", categoryMap);
                                                    data.putSerializable("segmentMap", segmentFilterMap);
                                                    data.putSerializable("filterData", filterData);
                                                    data.putString("origin", FilterViewModel.ORIGIN_HOME);
                                                    navigator.navigateActivity(ClassListActivity.class, data);
                                                }

                                            }
                                        }
                                ));


                            }

                        }
                        return results;
                    }
                });
            }
        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable
                                                 throwable) throws Exception {
                return new ArrayList<>();
            }
        }).mergeWith(getGridLoadingItems(6));
        ;
        gridViewModel = new GridViewModel(segments, categoryName);
    }

    private Observable<List<ViewModel>> getGridLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new TileShimmerItemViewModel()));
        return Observable.just(result);
    }
}
