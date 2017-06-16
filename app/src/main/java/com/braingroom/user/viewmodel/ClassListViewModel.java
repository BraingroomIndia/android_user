package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.dto.ListDialogData;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.FilterActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ClassListViewModel extends ViewModel {

    public final ListDialogData sortDialogData;
    public final Observable<List<ViewModel>> segments;
    public final ObservableBoolean segmentsVisibility = new ObservableBoolean(true);
    public final Observable<List<ViewModel>> classes;
    public final ObservableBoolean tileView = new ObservableBoolean(false);
    public final Action onViewChangeClicked, onSortClicked, onFilterClicked;
    public final ListDialogViewModel sortDialog;
    Consumer<List<String>> sortResultConsumer;
    ClassListActivity.UiHelper uiHelper;
    Navigator navigator;
    Function<List<ClassData>, List<ViewModel>> classMapFunction;
    FilterData filterData = new FilterData();

    public ClassListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator
            , @NonNull final HelperFactory helperFactory, @NonNull String categoryId, @NonNull String searchQuery, final ClassListActivity.UiHelper uiHelper) {
        if (searchQuery == null) searchQuery = "";
        if (categoryId == null) categoryId = "";
        if (categoryId.equals("-1")) {
            segmentsVisibility.set(false);
            categoryId = "";
        }
        this.uiHelper = uiHelper;
        this.navigator = navigator;
        segments = Observable.just(getDefaultSegments()).mergeWith(apiService.getSegments(categoryId))
                .map(new Function<SegmentResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(SegmentResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.getData().size() == 0) resp = getDefaultSegments();

                        for (final SegmentResp.Snippet elem : resp.getData()) {
                            results.add(new DataItemViewModel(" + " + elem.getSegmentName(), false, new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel dataItemViewModel) {

                                }
                            }, null));
                        }
                        return results;
                    }
                });

        classMapFunction = new Function<List<ClassData>, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                if (resp.size() == 0) resp = getDefaultClasses();
                for (final ClassData elem : resp) {
                    results.add(new ClassItemViewModel(elem, new Action() {
                        @Override
                        public void run() throws Exception {
                            if (!elem.getId().equals("-1")) {
                                Bundle data = new Bundle();
                                data.putString("id", elem.getId());
                                data.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                                navigator.navigateActivity(ClassDetailActivity.class, data);
                            }
                        }
                    }));
                }
                return results;
            }
        };

        classes = Observable.just(getDefaultClasses()).mergeWith(apiService.classSearch(categoryId, searchQuery))
                .map(classMapFunction);

        Map<String, String> sortKeys = new HashMap<>();
        sortKeys.put("Price - High to Low", "1");
        sortKeys.put("Price - Low to High", "2");
        this.sortResultConsumer = new Consumer<List<String>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<String> selectedIds) throws Exception {
                if (!selectedIds.get(0).equalsIgnoreCase("-1")) {
//                    filterData.setSortData(selectedIds.get(0));
//                    uiHelper.refreshSortedList(Observable.just(getDefaultClasses()).mergeWith(apiService.generalFilter(filterData.getFilterReq()))
//                            .map(classMapFunction),tileView.get());
                }
            }
        };

        sortDialogData = new ListDialogData(new Integer[]{-1}, sortKeys);
        sortDialog = new ListDialogViewModel(helperFactory.createDialogHelper(), "SORT BY", messageHelper, Observable.just(sortDialogData), false, this.sortResultConsumer);
        onViewChangeClicked = new Action() {
            @Override
            public void run() throws Exception {
                tileView.set(!tileView.get());
            }
        };

        onSortClicked = new Action() {
            @Override
            public void run() throws Exception {
                sortDialog.show();
            }
        };

        onFilterClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivityForResult(FilterActivity.class, null, REQ_CODE_CHOOSE_FILTER);
            }
        };


    }

    private SegmentResp getDefaultSegments() {
        List<SegmentResp.Snippet> data = new ArrayList<>();
        data.add(new SegmentResp.Snippet("-1", "loading segments...", null));
        return new SegmentResp("", data);
    }

    private List<ClassData> getDefaultClasses() {
        return Collections.nCopies(1, new ClassData());
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_CHOOSE_FILTER) {
            if (data != null && data.getSerializableExtra("filter") != null) {
//                FilterData filterData = (FilterData) data.getSerializableExtra("filter");
//                filterData.setFilterData(filterData);
//                uiHelper.refreshSortedList(Observable.just(getDefaultClasses()).mergeWith(apiService.generalFilter(filterData.getFilterReq()))
//                        .map(classMapFunction),tileView.get());
            }

        }
    }


}
