package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;
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

    public static final String ORIGIN_COMMUNITY = "HOME Community";
    public static final String ORIGIN_CATEGORY = "HOME Category";
    public static final String ORIGIN_HOME = "HOME Home";

    public static final int CLASS_SCHECULE_FIXED = 2;
    public static final int CLASS_SCHECULE_FLEXIBLE = 1;
    private final String origin;
    public final ObservableField<String> keywords;
    public final ListDialogViewModel1 categoryVm, segmentsVm, communityVm, classTypeVm, classScheduleVm;
    public final DatePickerViewModel startDateVm, endDateVm;
    public final Action onBackClicked, onResetClicked, onApplyClicked;
    public final Navigator navigator;
    public final MessageHelper messageHelper;
    public Consumer<HashMap<String, Integer>> categoryConsumer;
    public Consumer<HashMap<String, Pair<String, String>>> cityConsumer;
    private boolean clearFlag = false;
    public final SearchSelectListViewModel cityVm, localityVm, vendorListVm;
    public Observable<HashMap<String, Pair<String, String>>> cityApiObservable, localityApiObservable, vendorlistApiObservable;
    public ObservableBoolean isCatalogue;

    public final boolean isCommunityVisible;
    public final boolean isCategoryVisible;

    private final FilterData filterData;

    public FilterViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, FragmentHelper fragmentHelper
            , FilterData filterData, HashMap<String, Integer> categoryFilterMap,
                           HashMap<String, Integer> segmentsFilterMap,
                           HashMap<String, String> cityFilterMap,
                           HashMap<String, String> localityFilterMap,
                           HashMap<String, Integer> communityFilterMap,
                           HashMap<String, Integer> classTypeMap,
                           HashMap<String, Integer> classScheduleMap,
                           HashMap<String, String> vendorListMap
                     /*      String keywords,
                           String startDate,
                           String endDate*/

            , final String origin) {
        this.filterData = filterData;
        isCatalogue = new ObservableBoolean(false);
        this.origin = origin;
        if (origin.equals(ClassListViewModel1.ORIGIN_CATALOG))
            isCatalogue.set(true);
        isCommunityVisible = !(origin.equals(ORIGIN_COMMUNITY));
        isCategoryVisible = !(origin.equals(ORIGIN_CATEGORY));

        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        segmentsFilterMap = segmentsFilterMap != null ? segmentsFilterMap : new HashMap<String, Integer>();
        categoryFilterMap = categoryFilterMap != null ? categoryFilterMap : new HashMap<String, Integer>();
        communityFilterMap = communityFilterMap != null ? communityFilterMap : new HashMap<String, Integer>();
        classTypeMap = classTypeMap != null ? classTypeMap : new HashMap<String, Integer>();
        classScheduleMap = classScheduleMap != null ? classScheduleMap : new HashMap<String, Integer>();
        vendorListMap = vendorListMap != null ? vendorListMap : new HashMap<String, String>();
        cityFilterMap = cityFilterMap != null ? cityFilterMap : new HashMap<String, String>();
        localityFilterMap = localityFilterMap != null ? localityFilterMap : new HashMap<String, String>();
        this.keywords = new ObservableField<>(filterData != null ? filterData.getKeywords() : "");
        this.navigator = navigator;
        this.messageHelper = messageHelper;
        startDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "Start", filterData != null ? filterData.getStartDate() : "YYYY-MM-DD");
        endDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "End", filterData != null ? filterData.getEndDate() : "YYYY-MM-DD");
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
                }), categoryFilterMap, false, categoryConsumer, "");


        segmentsVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, getSegmentsApiObservable(filterData != null ? filterData.getCategoryId() : null), segmentsFilterMap, false, null, "select a catgory first");


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
                    localityVm.changeDataSource(localityApiObservable);
                } else {
                    localityVm.changeDataSource(null);
                }
            }
        };

        cityApiObservable = getCityApiObservable();


        cityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_CITY, messageHelper, navigator, "search for city", false, cityApiObservable, "", cityConsumer, fragmentHelper);
        localityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_LOCALITY, messageHelper, navigator, "search for locality", false, localityApiObservable, "select a city first", null, fragmentHelper);
        cityVm.setSelectedValues(cityFilterMap);
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
        vendorListVm.setSelectedValues(vendorListMap);

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
        }), communityFilterMap, false, null, "");

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
        classTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class Type", messageHelper, Observable.just(new ListDialogData1(ClassTypeApiData)), classTypeMap, false, null, "");

        LinkedHashMap<String, Integer> ClassScheduleApiData = new LinkedHashMap<>();
        ClassScheduleApiData.put("Fixed", CLASS_SCHECULE_FIXED);
        ClassScheduleApiData.put("Flexible", CLASS_SCHECULE_FLEXIBLE);
        classScheduleVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class Schedule", messageHelper, Observable.just(new ListDialogData1(ClassScheduleApiData)), classScheduleMap, false, null, "");

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
        if (categoryId == null)
            return null;
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
       /* HashMap<String,String> emptyHashMap= new HashMap<>();
        emptyHashMap.put("","");*/
        keywords.set("");
        categoryVm.reset();
        segmentsVm.reset();
        cityVm.clearSelectedValue();
        localityVm.changeDataSource(null);
        communityVm.reset();
        classTypeVm.reset();
        classScheduleVm.reset();
        vendorListVm.clearSelectedValue();
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

    private Observable<HashMap<String, Pair<String, String>>> getCityApiObservable() {
        if (isCatalogue.get())
            return apiService.getCatalogueCities().map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
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
        else
            return apiService.getCityList("35").map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
                @Override
                public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                    if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                    HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                    for (CommonIdResp.Snippet snippet : resp.getData()) {
                        if (snippet.getId().equals("3659"))
                            resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                    }
                    return resMap;
                }
            });
    }

    public void apply() {
        FilterData filterData = new FilterData();
        Intent resultIntent = new Intent();
        if (this.filterData != null)
            filterData.setCatalog(this.filterData.getCatalog());
        filterData.setCommunityId(communityVm.selectedItemsMap.isEmpty() ? "" : communityVm.selectedItemsMap.values().iterator().next() + "");
        filterData.setClassType(classTypeVm.selectedItemsMap.isEmpty() ? "" : classTypeVm.selectedItemsMap.values().iterator().next() + "");
        filterData.setClassSchedule(classScheduleVm.selectedItemsMap.isEmpty() ? "" : classScheduleVm.selectedItemsMap.values().iterator().next() + "");
        filterData.setClassProvider(getHashMap(vendorListVm.selectedDataMap).isEmpty() ? "" : getHashMap(vendorListVm.selectedDataMap).values().iterator().next() + "");
        filterData.setCity(getHashMap(cityVm.selectedDataMap).isEmpty() ? "" : getHashMap(cityVm.selectedDataMap).values().iterator().next() + "");
        filterData.setLocationId(getHashMap(localityVm.selectedDataMap).isEmpty() ? "" : getHashMap(localityVm.selectedDataMap).values().iterator().next() + "");
        filterData.setCategoryId(categoryVm.selectedItemsMap.isEmpty() ? "" : categoryVm.selectedItemsMap.values().iterator().next() + "");
        filterData.setSegmentId(segmentsVm.selectedItemsMap.isEmpty() ? "" : segmentsVm.selectedItemsMap.values().iterator().next() + "");
        filterData.setKeywords(keywords.get());
        filterData.setStartDate(startDateVm.date.get().equals("YYYY-MM-DD") ? "" : startDateVm.date.get());
        filterData.setEndDate(endDateVm.date.get().equals("YYYY-MM-DD") ? "" : endDateVm.date.get());

        Bundle bundle = new Bundle();
        bundle.putSerializable("category", categoryVm.selectedItemsMap);
        bundle.putSerializable("segment", segmentsVm.selectedItemsMap);
        bundle.putSerializable("city", getHashMap(cityVm.selectedDataMap));
        bundle.putSerializable("locality", getHashMap(localityVm.selectedDataMap));
        bundle.putSerializable("community", communityVm.selectedItemsMap);
        bundle.putSerializable("classType", classTypeVm.selectedItemsMap);
        bundle.putSerializable("classSchedule", classScheduleVm.selectedItemsMap);
        bundle.putSerializable("vendorList", getHashMap(vendorListVm.selectedDataMap));
        bundle.putString("origin", origin);
        /*bundle.putSerializable("keywords", keywords.get());
        bundle.putString("startDate", startDateVm.date.get().equals("YYYY-MM-DD") ? "" : startDateVm.date.get());
        bundle.putString("endDate", endDateVm.date.get().equals("YYYY-MM-DD") ? "" : endDateVm.date.get());*/
        bundle.putBoolean("clearFlag", clearFlag);
        bundle.putSerializable("filterData", filterData);
        if (origin.equals(ORIGIN_HOME)) {
            navigator.navigateActivity(ClassListActivity.class, bundle);
            navigator.finishActivity();
            return;
        }
        resultIntent.putExtras(bundle);
        navigator.finishActivity(resultIntent);
        navigator.finishActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectivityViewmodel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        connectivityViewmodel.onPause();
    }

    @Override
    public void retry() {
        connectivityViewmodel.isConnected.set(true);
        callAgain.set(callAgain.get() + 1);
    }
}
