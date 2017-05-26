package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.FilterActivity;
import com.braingroom.user.viewmodel.fragment.SearchSelectListViewModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class FilterViewModel extends ViewModel {

    public static final int CLASS_TYPE_CLASSES = 4;
    public static final int CLASS_TYPE_SEMINAR = 2;
    public static final int CLASS_TYPE_WEBINAR = 3;
    public static final int CLASS_TYPE_WORKSHOP = 1;
    public static final int CLASS_TYPE_ACTIVITY = 5;

    public static final int CLASS_SCHECULE_FIXED = 2;
    public static final int CLASS_SCHECULE_FLEXIBLE = 1;

    public final ObservableField<String> keywords;
    public final ListDialogViewModel1 categoryVm, segmentsVm, communityVm, classTypeVm, classScheduleVm;
    public final DatePickerViewModel startDateVm, endDateVm;
    public final Action onBackClicked, onResetClicked, onApplyClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Integer>> categoryConsumer;
    public Consumer<HashMap<String, Pair<String, String>>> cityConsumer;
    private boolean clearFlag = false;
    public final SearchSelectListViewModel cityVm, localityVm, vendorListVm;
    public Observable<HashMap<String, Pair<String, String>>> cityApiObservable, localityApiObservable, vendorlistApiObservable;

    public FilterViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, FragmentHelper fragmentHelper
            , HashMap<String, Integer> categoryFilterMap,
                           HashMap<String, Integer> segmentsFilterMap,
                           HashMap<String, String> cityFilterMap,
                           HashMap<String, String> localityFilterMap,
                           HashMap<String, Integer> communityFilterMap,
                           HashMap<String, Integer> classTypeMap,
                           HashMap<String, Integer> classScheduleMap,
                           HashMap<String, String> vendorListMap,
                           String keywords,
                           String startDate,
                           String endDate

    ) {
        this.keywords = new ObservableField<>(keywords);
        this.navigator = navigator;
        startDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "Start", startDate);
        endDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "End", endDate);
        categoryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    segmentsVm.reInit(getSegmentsApiObservable(selectedId));
                }
            }
        };
        categoryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Category", messageHelper, apiService.getCategory()
                .map(new Function<CategoryResp, ListDialogData1>() {
                    @Override
                    public ListDialogData1 apply(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                        LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                        for (CategoryResp.Snippet snippet : categoryResp.getData()) {
                            itemMap.put(snippet.getCategoryName(), Integer.parseInt(snippet.getId()));
                        }
                        // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                        return new ListDialogData1(itemMap);
                    }
                }), categoryFilterMap, false, categoryConsumer);


        segmentsVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, getSegmentsApiObservable("-1"), segmentsFilterMap, false, null);


        cityConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    localityApiObservable = apiService.getLocalityList(selectedId).map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
                        @Override
                        public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                            if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                            HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                            for (CommonIdResp.Snippet snippet : resp.getData()) {
                                resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                            }
                            return resMap;
                        }
                    });
                    localityVm.refreshDataMap(cityApiObservable);
                } else {
                    localityVm.refreshDataMap(null);
                }
            }
        };

        cityApiObservable = apiService.getCityList("3659").map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
            @Override
            public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                }
                return resMap;
            }
        });

        cityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_CITY, messageHelper, navigator, "search for city", false, cityApiObservable, "select a state first", cityConsumer, fragmentHelper);
        cityVm.setSelectedValues(cityFilterMap);
        localityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_LOCALITY, messageHelper, navigator, "search for locality", false, localityApiObservable, "select a city first", null, fragmentHelper);
        localityVm.setSelectedValues(localityFilterMap);

        vendorlistApiObservable = apiService.getVendors().map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
            @Override
            public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                }
                return resMap;
            }
        });
        vendorListVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_VENDORLIST, messageHelper, navigator, "search for vendors", false, vendorlistApiObservable, "", null, fragmentHelper);
        localityVm.setSelectedValues(vendorListMap);

        communityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Communities", messageHelper, apiService.getCommunity().map(new Function<CommunityResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommunityResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommunityResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), communityFilterMap, false, null);

//        vendorListVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class providers", messageHelper, apiService.getVendors().map(new Function<CommonIdResp, ListDialogData1>() {
//            @Override
//            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
//                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
//                for (CommonIdResp.Snippet snippet : resp.getData()) {
//                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
//                }
//                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
//                return new ListDialogData1(itemMap);
//            }
//        }), vendorListMap, false, null);

        LinkedHashMap<String, Integer> ClassTypeApiData = new LinkedHashMap<>();
        ClassTypeApiData.put("Classes", CLASS_TYPE_CLASSES);
        ClassTypeApiData.put("Online Classes", CLASS_TYPE_SEMINAR);
        ClassTypeApiData.put("Webinar", CLASS_TYPE_WEBINAR);
        ClassTypeApiData.put("Workshops", CLASS_TYPE_WORKSHOP);
        ClassTypeApiData.put("Learning events & activities", CLASS_TYPE_ACTIVITY);
        classTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class Type", messageHelper, Observable.just(new ListDialogData1(ClassTypeApiData)), classTypeMap, false, null);

        LinkedHashMap<String, Integer> ClassScheduleApiData = new LinkedHashMap<>();
        ClassScheduleApiData.put("Fixed", CLASS_SCHECULE_FIXED);
        ClassScheduleApiData.put("Flexible", CLASS_SCHECULE_FLEXIBLE);
        classScheduleVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class Schedule", messageHelper, Observable.just(new ListDialogData1(ClassScheduleApiData)), classScheduleMap, false, null);

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };
        onResetClicked = new Action() {
            @Override
            public void run() throws Exception {
                reset();
            }
        };
        onApplyClicked = new Action() {
            @Override
            public void run() throws Exception {
                apply();
            }
        };

    }

    private Observable<ListDialogData1> getSegmentsApiObservable(String categoryId) {
        return apiService.getSegments(categoryId).map(new Function<SegmentResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull SegmentResp segmentsResp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (SegmentResp.Snippet snippet : segmentsResp.getData()) {
                    itemMap.put(snippet.getSegmentName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    private Observable<ListDialogData1> getLocalityApiObservable(String cityId) {
        return apiService.getLocalityList(cityId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    public void reset() {
        keywords.set("");
        categoryVm.reset();
        segmentsVm.reset();
        cityVm.refreshDataMap(null);
        localityVm.refreshDataMap(null);
        communityVm.reset();
        classTypeVm.reset();
        classScheduleVm.reset();
        vendorListVm.refreshDataMap(null);
        startDateVm.reset();
        endDateVm.reset();
        clearFlag = true;
    }

    private HashMap<String, String> getHashMap(HashMap<String, Pair<String, String>> source) {
        HashMap<String, String> resultMap = new HashMap<>();
        for (String key : source.keySet()) {
            resultMap.put(key, source.get(key).first);
        }
        return resultMap;
    }

    public void apply() {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", categoryVm.selectedItemsMap);
        bundle.putSerializable("segment", segmentsVm.selectedItemsMap);
        bundle.putSerializable("city", getHashMap(cityVm.selectedDataMap));
        bundle.putSerializable("locality", getHashMap(localityVm.selectedDataMap));
        bundle.putSerializable("community", communityVm.selectedItemsMap);
        bundle.putSerializable("keywords", keywords.get());
        bundle.putSerializable("classType", classTypeVm.selectedItemsMap);
        bundle.putSerializable("classSchedule", classScheduleVm.selectedItemsMap);
        bundle.putSerializable("vendorList", getHashMap(vendorListVm.selectedDataMap));
        bundle.putString("startDate", startDateVm.date.get().equals("YYYY-MM-DD") ? "" : startDateVm.date.get());
        bundle.putString("endDate", endDateVm.date.get().equals("YYYY-MM-DD") ? "" : endDateVm.date.get());
        bundle.putBoolean("clearFlag", clearFlag);
        resultIntent.putExtras(bundle);
        navigator.finishActivity(resultIntent);
    }
}
