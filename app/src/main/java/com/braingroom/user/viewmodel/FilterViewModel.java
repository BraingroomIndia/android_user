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
import com.braingroom.user.utils.ClassSchedule;
import com.braingroom.user.utils.ClassType;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.Constants;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class FilterViewModel extends ViewModel {


    public static final String ORIGIN_COMMUNITY = "HOME Community";
    public static final String ORIGIN_CATEGORY = "HOME Category";
    public static final String ORIGIN_FILTER = "Home Filter";
    public static final String ORIGIN_HOME = "HOME Home";
    public static final String ORIGIN_CATALOG = "CATALOG";
    private final String origin;
    public final ObservableField<String> keywords;
    public final ListDialogViewModel1 categoryVm, segmentsVm, communityVm, classTypeVm, classScheduleVm;
    public final DatePickerViewModel startDateVm, endDateVm;
    public final Action onBackClicked, onResetClicked, onApplyClicked;
    public final Navigator navigator;
    public final MessageHelper messageHelper;
    public final Consumer<HashMap<String, Integer>> categoryConsumer, segmentConsumer, classScheduleConsumer, communityConsumer, classTypeConsumer;
    public final Consumer<HashMap<String, Pair<Integer, String>>> cityConsumer, localityConsumer, vendorConsumer;
    private boolean clearFlag = false;
    public final SearchSelectListViewModel cityVm, localityVm, vendorListVm;
    public Observable<HashMap<String, Pair<Integer, String>>> cityApiObservable, localityApiObservable, vendorlistApiObservable;
    public ObservableBoolean isCatalogue;

    public final boolean isCommunityVisible;
    public final boolean isCategoryVisible;

    private final FilterData filterData;
    private boolean cityConsumer1 = false;/////TODO remove

    public FilterViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, FragmentHelper fragmentHelper
            , @NonNull final FilterData filterData1
                     /*      String keywords,
                           String startDate,
                           String endDate*/

            , final String origin) {
        this.filterData = filterData1;
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
        this.keywords = new ObservableField<>(filterData.getKeywords());
        this.navigator = navigator;
        this.messageHelper = messageHelper;
        startDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "Start", filterData.getStartDate());
        endDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "End", filterData.getEndDate());
        categoryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    segmentsVm.reInit(getSegmentsApiObservable(selectedId));
                }
                filterData.setCategoryFilterMap(selectedMap);
                filterData.setSegmentsFilterMap(null);
            }
        };
        categoryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Category", messageHelper, apiService.getCategory()
                .map(new Function<CategoryResp, ListDialogData1>() {
                    @Override
                    public ListDialogData1 apply(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                        LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                        for (CategoryResp.Snippet snippet : categoryResp.getData()) {
                            itemMap.put(snippet.getCategoryName(), snippet.getId());
                        }
                        return new ListDialogData1(itemMap);
                    }
                }), filterData.getCategoryFilterMap(), false, categoryConsumer, "");


        segmentConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(HashMap<String, Integer> stringIntegerHashMap) throws Exception {
                filterData.setSegmentsFilterMap(stringIntegerHashMap);
            }
        };
        classScheduleConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(HashMap<String, Integer> stringIntegerHashMap) throws Exception {
                filterData.setClassScheduleFilterMap(stringIntegerHashMap);
            }
        };
        segmentsVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, getSegmentsApiObservable(filterData.getCategoryId() + ""), filterData.getSegmentsFilterMap(), false, segmentConsumer, "select a category first");


        cityConsumer = new Consumer<HashMap<String, Pair<Integer, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<Integer, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    localityApiObservable = apiService.getLocalityList(selectedId).map(new Function<CommonIdResp, HashMap<String, Pair<Integer, String>>>() {
                        @Override
                        public HashMap<String, Pair<Integer, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                            if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                            HashMap<String, Pair<Integer, String>> resMap = new HashMap<>();
                            for (CommonIdResp.Snippet snippet : resp.getData()) {
                                resMap.put(snippet.getTextValue(), new Pair<Integer, String>(snippet.getId(), null));
                            }
                            return resMap;
                        }
                    });
                    localityVm.changeDataSource(localityApiObservable);
                } else {
                    localityVm.changeDataSource(null);
                }
                filterData.setCityFilterMap(CommonUtils.getSelectedDataMap(selectedMap));
                if (cityConsumer1) {
                    filterData.setLocalityFilterMap(null);
                    localityVm.clearSelectedValue();
                }
                cityConsumer1 = true;
            }
        };
        localityConsumer = new Consumer<HashMap<String, Pair<Integer, String>>>() {
            @Override
            public void accept(HashMap<String, Pair<Integer, String>> stringPairHashMap) throws Exception {
                filterData.setLocalityFilterMap(CommonUtils.getSelectedDataMap(stringPairHashMap));
            }
        };
        communityConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(HashMap<String, Integer> stringIntegerHashMap) throws Exception {
                filterData.setCommunityFilterMap(stringIntegerHashMap);
            }
        };
        classTypeConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(HashMap<String, Integer> hashMap) throws Exception {
                filterData.setClassTypeFilterMap(hashMap);
            }
        };
        vendorConsumer = new Consumer<HashMap<String, Pair<Integer, String>>>() {
            @Override
            public void accept(HashMap<String, Pair<Integer, String>> stringPairHashMap) throws Exception {
                filterData.setVendorFilterMap(CommonUtils.getSelectedDataMap(stringPairHashMap));
            }
        };

        cityApiObservable = getCityApiObservable();


        cityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_CITY, messageHelper, navigator, "search for city", false, cityApiObservable, "", cityConsumer, fragmentHelper);
        localityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_LOCALITY, messageHelper, navigator, "search for locality", false, localityApiObservable, "select a city first", localityConsumer, fragmentHelper);
        cityVm.setSelectedValues(filterData1.getCityFilterMap());
        localityVm.setSelectedValues(filterData1.getLocalityFilterMap());

        vendorlistApiObservable = apiService.getVendors().map(new Function<CommonIdResp, HashMap<String, Pair<Integer, String>>>() {
            @Override
            public HashMap<String, Pair<Integer, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<Integer, String>> resMap = new HashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getTextValue(), new Pair<Integer, String>(snippet.getId(), null));
                }
                return resMap;
            }
        });
        vendorListVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_VENDORLIST, messageHelper, navigator, "search for vendors", false, vendorlistApiObservable, "", vendorConsumer, fragmentHelper);
        vendorListVm.setSelectedValues(filterData1.getVendorFilterMap());

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
        }), filterData1.getCommunityFilterMap(), false, communityConsumer, "");

        LinkedHashMap<String, Integer> ClassTypeApiData = new LinkedHashMap<>();
        ClassTypeApiData.put(ClassType.WORKSHOP.name, ClassType.WORKSHOP.id);
        ClassTypeApiData.put(ClassType.SEMINAR.name, ClassType.SEMINAR.id);
        ClassTypeApiData.put(ClassType.WEBINAR.name, ClassType.WEBINAR.id);
        ClassTypeApiData.put(ClassType.CLASSES.name, ClassType.CLASSES.id);
        ClassTypeApiData.put(ClassType.ACTIVITY.name, ClassType.ACTIVITY.id);
        classTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class Type", messageHelper, Observable.just(new ListDialogData1(ClassTypeApiData)), filterData1.getClassTypeFilterMap(), false, classTypeConsumer, "");

        LinkedHashMap<String, Integer> ClassScheduleApiData = new LinkedHashMap<>();
        ClassScheduleApiData.put(ClassSchedule.Fixed.name, ClassSchedule.Fixed.id);
        ClassScheduleApiData.put(ClassSchedule.Flexible.name, ClassSchedule.Flexible.id);
        classScheduleVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class Schedule", messageHelper, Observable.just(new ListDialogData1(ClassScheduleApiData)), filterData1.getClassScheduleFilterMap(), false, classScheduleConsumer, "");

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
                    itemMap.put(snippet.getSegmentName(), snippet.getId());
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
                    itemMap.put(snippet.getTextValue(), snippet.getId());
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    public void reset() {
       /* HashMap<String,String> emptyHashMap= new HashMap<>();
        emptyHashMap.put("","");*/
        filterData.setFilterData(new FilterData());
        keywords.set("");
        categoryVm.reset();
        segmentsVm.reset();
        cityVm.clearSelectedValue();
        localityVm.changeDataSource(null);
        localityVm.clearSelectedValue();
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

    private Observable<HashMap<String, Pair<Integer, String>>> getCityApiObservable() {
        if (isCatalogue.get())
            return apiService.getCatalogueCities().map(new Function<CommonIdResp, HashMap<String, Pair<Integer, String>>>() {
                @Override
                public HashMap<String, Pair<Integer, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                    if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                    HashMap<String, Pair<Integer, String>> resMap = new HashMap<>();
                    for (CommonIdResp.Snippet snippet : resp.getData()) {
                        resMap.put(snippet.getTextValue(), new Pair<Integer, String>(snippet.getId(), null));
                    }
                    return resMap;
                }
            });
        else
            return apiService.getCityList().observeOn(AndroidSchedulers.mainThread()).map(new Function<CommonIdResp, HashMap<String, Pair<Integer, String>>>() {
                @Override
                public HashMap<String, Pair<Integer, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                    if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                    HashMap<String, Pair<Integer, String>> resMap = new HashMap<>();
                    for (CommonIdResp.Snippet snippet : resp.getData()) {
                        resMap.put(snippet.getTextValue(), new Pair<Integer, String>(snippet.getId(), null));
                    }
                    return resMap;
                }
            });
    }

    public void apply() {
        Bundle bundle = new Bundle();

        Intent resultIntent = new Intent();
        if (this.filterData != null) {
            filterData.setKeywords(keywords.get());
            filterData.setEndDate(endDateVm.date.get());
            filterData.setStartDate(startDateVm.date.get());
        }
        if (!ORIGIN_FILTER.equalsIgnoreCase(origin))
            bundle.putString(Constants.origin, origin);
        else bundle.putString(Constants.origin, ORIGIN_HOME);
        bundle.putBoolean("clearFlag", clearFlag);
        bundle.putSerializable(Constants.classFilterData, filterData);
        resultIntent.putExtras(bundle);
        if (ORIGIN_FILTER.equalsIgnoreCase(origin))
            navigator.navigateActivity(ClassListActivity.class, bundle);
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
