package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassListData;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.FilterActivity;
import com.braingroom.user.view.adapters.ViewProvider;
import com.braingroom.user.viewmodel.fragment.SearchSelectListViewModel;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;

public class ClassListViewModel1 extends ViewModel {

    public static final int LAYOUT_TYPE_ROW = -1;
    public static final int LAYOUT_TYPE_TILE = 1;
    public static final int SORT_ORDER_LOW_TO_HIGH = 2;
    public static final int SORT_ORDER_HIGH_TO_LOW = 1;

    public static final String ORIGIN_HOME = "HOME";
    public static final String ORIGIN_CATALOG = "CATALOG";
    public static final String ORIGIN_GIFT = "GIFT";


    public final
    @NonNull
    ObservableInt layoutType;

    public Observable<List<ViewModel>> classes;
    public final Observable<List<ViewModel>> segments;
    public final Observable<List<ViewModel>> selectedFilter;
    public final ObservableBoolean segmentsVisibility = new ObservableBoolean(true);
    public final ObservableBoolean selectedFilterisEmpty = new ObservableBoolean(false);

    public final Action onViewChangeClicked, onSortClicked, onFilterClicked;
    public final Function<ClassListData, List<ViewModel>> classDataMapFunction;
    FilterData filterData = new FilterData();

    public final ListDialogViewModel1 sortDialogVm;
    public final Consumer<HashMap<String, Integer>> sortResultConsumer;
    public final ClassListActivity.UiHelper uiHelper;

    private boolean paginationInProgress = false;

    public HashMap<String, Integer> categoryFilterMap = new HashMap<>();
    public HashMap<String, Integer> segmentsFilterMap = new HashMap<>();
    public HashMap<String, String> cityFilterMap = new HashMap<>();
    public HashMap<String, String> localityFilterMap = new HashMap<>();
    public HashMap<String, Integer> communityFilterMap = new HashMap<>();
    public HashMap<String, Integer> classTypeFilterMap = new HashMap<>();
    public HashMap<String, Integer> classScheduleFilterMap = new HashMap<>();
    public HashMap<String, String> vendorListFilterMap = new HashMap<>();
    public final SearchSelectListViewModel localityVm;
    public final Observable<HashMap<String, Pair<String, String>>> localityApiObservable;
    public String keywords = "";
    public String startDate = "";
    public String endDate = "";
    public ObservableField<String> searchQuery = new ObservableField<>("");
    public ObservableBoolean hideSearchBar = new ObservableBoolean(true);
    PublishSubject<DataItemViewModel> segmentSelectorSubject = PublishSubject.create();

    private String localityName = pref.getString(Constants.LOCALITY_NAME, "");
    private String localityId = pref.getString(Constants.LOCALITY_ID, "");


    @Getter
    ViewProvider viewProvider;

    private int nextPage = 0;
    private int currentPage = -1;
    private boolean segmentAvailable = false;
    private boolean isCatalogue = false;
    private String origin;

    public final Navigator navigator;

    private boolean isLocalitySelected;
    private boolean askedForLocality = false;


    public ClassListViewModel1(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator
            , @NonNull final HelperFactory helperFactory, @NonNull final FilterData filterData1,
                               final HashMap<String, Integer> categoryMap,
                               final HashMap<String, Integer> segmentsMap,
                               HashMap<String, String> cityMap,
                               HashMap<String, String> localityMap,
                               HashMap<String, Integer> communityMap,
                               HashMap<String, Integer> classTypeMap,
                               HashMap<String, Integer> classScheduleMap,
                               HashMap<String, String> vendorListMap,
                               @Nullable final String origin,
                               @NonNull final ClassListActivity.UiHelper uiHelper,
                               @NonNull final FragmentHelper fragmentHelper) {


        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        localityName = pref.getString(Constants.LOCALITY_NAME, "");
        localityId = pref.getString(Constants.LOCALITY_ID, "");
        isLocalitySelected = !TextUtils.isEmpty(localityId);
        this.navigator = navigator;
        this.origin = origin;
        if (this.origin != null && this.origin.equals(ORIGIN_CATALOG))
            isCatalogue = true;
        categoryFilterMap = categoryMap != null ? categoryMap : new HashMap<String, Integer>();
        //searchQuery.set(filterData1.getKeywords());
        segmentsFilterMap = segmentsMap != null ? segmentsMap : new HashMap<String, Integer>();
        cityFilterMap = cityMap != null ? cityMap : new HashMap<String, String>();
        localityFilterMap = localityMap != null ? localityMap : new HashMap<String, String>();
        communityFilterMap = communityMap != null ? communityMap : new HashMap<String, Integer>();
        classTypeFilterMap = classTypeMap != null ? classTypeMap : new HashMap<String, Integer>();
        classScheduleFilterMap = classScheduleMap != null ? classScheduleMap : new HashMap<String, Integer>();
        vendorListFilterMap = vendorListMap != null ? vendorListMap : new HashMap<String, String>();
        viewProvider = new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                if (layoutType.get() == LAYOUT_TYPE_ROW) {
                    if (vm instanceof ClassItemViewModel) {
                        if (ORIGIN_CATALOG.equals(origin))
                            return R.layout.item_class_row_catalog;
                        if ((origin != null && origin.contains(ORIGIN_HOME)) || ORIGIN_GIFT.equals(origin))
                            return R.layout.item_class_row_listing;
                    } else if (vm instanceof EmptyItemViewModel)
                        return R.layout.item_empty_data;
                    else if (vm instanceof RowShimmerItemViewModel)
                        return R.layout.item_shimmer_row;
                } else {
                    if (vm instanceof ClassItemViewModel) {
                        if (ORIGIN_CATALOG.equals(origin))
                            return R.layout.item_class_tile_catalog;
                        if ((origin != null && origin.contains(ORIGIN_HOME) || ORIGIN_GIFT.equals(origin)))
                            return R.layout.item_class_tile_listing;
                    } else if (vm instanceof EmptyItemViewModel)
                        return R.layout.item_empty_data;
                    else if (vm instanceof TileShimmerItemViewModel)
                        return R.layout.item_shimmer_tile;
                }
                return 0;
            }
        };

        localityApiObservable = apiService.getLocalityList(3659 + "").map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
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


        layoutType = new ObservableInt(LAYOUT_TYPE_TILE);
        nonReactiveItems = new ArrayList<>();

        filterData = filterData1;
        if (!ORIGIN_CATALOG.equals(origin) && TextUtils.isEmpty(filterData.getLocationId()) && !TextUtils.isEmpty(localityId)) {
            filterData.setLocationId(localityId);
            localityFilterMap = new HashMap<>();
            localityFilterMap.put(localityName, localityId);
        }

        localityVm = new SearchSelectListViewModel(FilterActivity.FRAGMENT_TITLE_LOCALITY, messageHelper, navigator, "search for locality", false, localityApiObservable, "select a city first", new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    String selectedName = "" + selectedMap.keySet().iterator().next();
                    editor.putString(Constants.LOCALITY_ID, selectedId);
                    editor.putString(Constants.LOCALITY_NAME, selectedName);
                    editor.commit();
                    localityFilterMap = new HashMap<>();
                    localityFilterMap.put(selectedName, selectedId);
                    filterData.setLocationId(selectedId);
                    isLocalitySelected = true;
                    reset();
                }
            }
        }, fragmentHelper);
/*        filterData.setCategoryId(categoryId);
        filterData.setCommunityId(communityId);
        filterData.setSegmentId(segmentId);
        filterData.setKeywords(searchQuery);
        filterData.setCatalog(catalog);
        filterData.setGiftId(giftId);*/
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });


        this.uiHelper = uiHelper;
        /* if coming from community click
        * hide macro selects and call */
        if (filterData != null && (!filterData.getCommunityId().equals("") || !filterData.getCatalog().equals("") || filterData.getCategoryId().equals(""))) {
            segmentsVisibility.set(false);
        }
        segments = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return (!segmentAvailable) && segmentsVisibility.get();
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return Observable.just(getDefaultSegments()).mergeWith(apiService.getSegments(filterData.getCategoryId()))
                        .map(new Function<SegmentResp, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(SegmentResp resp) throws Exception {
                                List<ViewModel> results = new ArrayList<>();
                                if (resp.getData().size() == 0) resp = getDefaultSegments();

                                for (final SegmentResp.Snippet elem : resp.getData()) {
                                    if (!elem.getId().equals("-1"))
                                        segmentAvailable = true;
                                    results.add(new DataItemViewModel(" + " + elem.getSegmentName(), false, new MyConsumer<DataItemViewModel>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                            if (!elem.getId().equals("-1")) {
                                                filterData.setSegmentId(elem.getId());
                                                filterData.setKeywords("");
                                                segmentSelectorSubject.onNext(viewModel);
                                                reset();
                                            }
                                        }
                                    }, segmentSelectorSubject));
                                }
                                return results;
                            }
                        });
            }
        });
        selectedFilter = FieldUtils.toObservable(callAgain).
                flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
                    @Override
                    public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        List<ViewModel> selectedItems = new ArrayList<ViewModel>();
                        if (categoryFilterMap != null && !categoryFilterMap.isEmpty()) {
                            if (segmentsFilterMap != null && !segmentsFilterMap.isEmpty()) {
                                selectedItems.add(new DataItemViewModel(categoryFilterMap.keySet().iterator().next() + " -> " + segmentsFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                        segmentsFilterMap = new HashMap<String, Integer>();
                                        filterData.setSegmentId("");
                                        reset();
                                    }
                                }));
                            } else

                                selectedItems.add(new DataItemViewModel(categoryFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                        categoryFilterMap = new HashMap<String, Integer>();
                                        filterData.setCategoryId("");
                                        reset();
                                    }
                                }));
                        }

                        if (localityFilterMap != null && !localityFilterMap.isEmpty()) {
                            selectedItems.add(new DataItemViewModel(localityFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                    localityFilterMap = new HashMap<String, String>();
                                    editor.putString(Constants.LOCALITY_ID, "");
                                    editor.putString(Constants.LOCALITY_NAME, "");
                                    editor.commit();
                                    filterData.setLocationId("");
                                    reset();
                                }
                            }));
                        }
                        if (communityFilterMap != null && !communityFilterMap.isEmpty()) {
                            selectedItems.add(new DataItemViewModel(communityFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                    communityFilterMap = new HashMap<String, Integer>();
                                    filterData.setCommunityId("");
                                    reset();
                                }
                            }));
                        }
                        if (classScheduleFilterMap != null && !classScheduleFilterMap.isEmpty()) {
                            selectedItems.add(new DataItemViewModel(classScheduleFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                    classScheduleFilterMap = new HashMap<String, Integer>();
                                    filterData.setClassSchedule("");
                                    reset();
                                }
                            }));
                        }
                        if (classTypeFilterMap != null && !classTypeFilterMap.isEmpty()) {
                            selectedItems.add(new DataItemViewModel(classTypeFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                    classTypeFilterMap = new HashMap<String, Integer>();
                                    filterData.setClassType("");
                                    reset();
                                }
                            }));
                        }
                        if (vendorListFilterMap != null && !vendorListFilterMap.isEmpty()) {
                            selectedItems.add(new DataItemViewModel(vendorListFilterMap.keySet().iterator().next(), new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                    vendorListFilterMap = new HashMap<String, String>();
                                    filterData.setClassProvider("");
                                    reset();
                                }
                            }));
                        }
                        if (selectedItems.isEmpty())
                            selectedFilterisEmpty.set(true);
                        else selectedFilterisEmpty.set(false);
                        return Observable.just(selectedItems);
                    }
                });
//
        classDataMapFunction = new Function<ClassListData, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(ClassListData resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                currentPage = nextPage;
                nextPage = resp.getNextPage();
                Bundle bundle = new Bundle();
                // FireBase Tracking
                String screenName = "";
                if (currentPage < 2 && !isCatalogue) {


                    if (!categoryFilterMap.isEmpty()) {
                        String temp = categoryFilterMap.keySet().iterator().next();
                        screenName = screenName + temp;
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Normal ClassList");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, temp);
                        if (!classTypeFilterMap.isEmpty()) {
                            temp = classTypeFilterMap.keySet().iterator().next();
                            screenName = screenName + temp;
                            bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, temp);
                        }
                        if (!localityFilterMap.isEmpty()) {
                            temp = localityFilterMap.keySet().iterator().next();
                            screenName = screenName + temp;
                            bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, localityFilterMap.values().iterator().next() + "");
                            bundle.putString(FirebaseAnalytics.Param.LOCATION, temp);
                        }
                    } else if (!communityFilterMap.isEmpty()) {
                        String temp = "Community ClassList" + communityFilterMap.keySet().iterator().next();
                        screenName = screenName + temp;
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Community ClassList");
                        bundle.putString(FirebaseAnalytics.Param.GROUP_ID, temp);
                        if (!classTypeFilterMap.isEmpty()) {
                            temp = classTypeFilterMap.keySet().iterator().next();
                            screenName = screenName + temp;
                            bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, temp);
                        }
                        if (!localityFilterMap.isEmpty()) {
                            temp = localityFilterMap.keySet().iterator().next();
                            screenName = screenName + temp;
                            bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, localityFilterMap.values().iterator().next() + "");
                            bundle.putString(FirebaseAnalytics.Param.LOCATION, temp);
                        }

                    } else if (isCatalogue && currentPage < 2) {
                        String temp = "Catalogue Listing";
                        screenName = screenName + temp + filterData.getCatalog();

                    }
                    if (!bundle.isEmpty())
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
                    if (!isEmpty(screenName)) {
                        setScreenName(screenName);
                    }

                }
                // FireBase Tracking
                Log.d(TAG, "\napply: nextPage:\t " + nextPage + "\n currentPage:\t" + currentPage);
                if (resp.getClassDataList().size() == 0 && currentPage == 0) {
                    results.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No classes Available", null));
                } else {
                    for (final ClassData elem : resp.getClassDataList()) {
                        //Edited By Vikas Godara
                        List<ClassLocationData> location = elem.getLocation();
                        if (elem.getClassType().equalsIgnoreCase("Online Classes"))
                            elem.setLocality("Online");
                        else if (elem.getClassType().equalsIgnoreCase("Webinars"))
                            elem.setLocality("Webinars");
                        else if (elem.getLocation().size() == 0)
                            elem.setLocality("");
                        else if (location != null)
                            elem.setLocality(location.get(0).getLocality());
                        //Edited By Vikas Godara
                        results.add(new ClassItemViewModel(elem, new Action() {
                            @Override
                            public void run() throws Exception {
                                Bundle data = new Bundle();
                                data.putString("id", elem.getId());
                                data.putString("origin", origin);
                                data.putString("catalogueId", filterData.getCatalog());
                                navigator.navigateActivity(ClassDetailActivity.class, data);
                            }
                        }));
                    }
                }
                return results;
            }
        };

        //initClassItemObserver();
//        classes = getLoadingItems().mergeWith(apiService.generalFilter(filterData.getFilterReq()).map(classDataMapFunction));
        sortResultConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> sortData) throws Exception {
                if (sortData.values().iterator().hasNext()) {
                    String sortOrder = "" + sortData.values().iterator().next();
                    if (isCatalogue)
                        filterData.setSortOrderCat(sortOrder);
                    else
                        filterData.setSortOrder(sortOrder);
                    reset();
                }
            }
        };
        LinkedHashMap<String, Integer> sortApiData = new LinkedHashMap<>();
        if (isCatalogue) {
            sortApiData.put("Time - New to Old ", SORT_ORDER_HIGH_TO_LOW);
            sortApiData.put("Time - Old to New", SORT_ORDER_LOW_TO_HIGH);
        } else {
            sortApiData.put("Price - High to Low", SORT_ORDER_HIGH_TO_LOW);
            sortApiData.put("Price - Low to High", SORT_ORDER_LOW_TO_HIGH);
        }
        sortDialogVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "SORT BY", messageHelper, Observable.just(new ListDialogData1(sortApiData)), new LinkedHashMap<String, Integer>(), false, this.sortResultConsumer, "");

        onViewChangeClicked = new Action() {
            @Override
            public void run() throws Exception {
                layoutType.set(-1 * layoutType.get());
                uiHelper.changeLayout(layoutType.get());
            }
        };

        onSortClicked = new Action() {
            @Override
            public void run() throws Exception {
                sortDialogVm.show();
            }
        };

        onFilterClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.origin, origin);
                filterData.setKeywords("");
                bundle.putSerializable(Constants.classFilterData, filterData);
                bundle.putSerializable(Constants.categoryFilterMap, categoryFilterMap);
                bundle.putSerializable(Constants.segmentsFilterMap, segmentsFilterMap);
                bundle.putSerializable(Constants.cityFilterMap, cityFilterMap);
                bundle.putSerializable(Constants.localityFilterMap, localityFilterMap);
                bundle.putSerializable(Constants.communityFilterMap, communityFilterMap);
                bundle.putSerializable(Constants.classTypeFilterMap, classTypeFilterMap);
                bundle.putSerializable(Constants.classScheduleFilterMap, classScheduleFilterMap);
                bundle.putSerializable(Constants.vendorListFilterMap, vendorListFilterMap);
                /*bundle.putString("keywords", keywords);
                bundle.putString("startDate", startDate);
                bundle.putString("endDate", endDate);*/

                navigator.navigateActivityForResult(FilterActivity.class, bundle, REQ_CODE_CHOOSE_FILTER);
            }
        };

        classes = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems().mergeWith(apiService.generalFilter(filterData.getFilterReq(), nextPage).map(classDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        return new ArrayList<ViewModel>(0);
                    }
                })).doOnNext(new Consumer<List<ViewModel>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<ViewModel> viewModels) throws Exception {

                        if (viewModels.size() > 0 && (viewModels.get(0) instanceof ClassItemViewModel || viewModels.get(0) instanceof EmptyItemViewModel) || nextPage == -1) {
                            Iterator<ViewModel> iter = nonReactiveItems.iterator();
                            while (iter.hasNext()) {
                                if (iter.next() instanceof ShimmerItemViewModel) {
                                    iter.remove();
                                }
                            }
                            if (viewModels.size() < 2) {
                                layoutType.set(LAYOUT_TYPE_ROW);
                                uiHelper.changeLayout(layoutType.get());
                            }
                            paginationInProgress = false;
                        }
                        nonReactiveItems.addAll(viewModels);
                        uiHelper.notifyDataChanged();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.d("Class Fetch error", "accept: " + throwable.getMessage());
                    }
                });
            }
        });
        classes.subscribe();
        //messageHelper.showAcceptDismissInfo();

     /*   FieldUtils.toObservable(callAgain).debounce(1, TimeUnit.SECONDS).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return false *//*!(isLocalitySelected || askedForLocality)*//*;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                askedForLocality = true;
//                fragmentHelper.show(SignupActivity.FRAGMENT_TITLE_COUNTRY);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
*/
    }


    public void paginate() {
        if (nextPage > 0 && !paginationInProgress) {
            paginationInProgress = true;
            retry();
        }
    }

    private SegmentResp getDefaultSegments() {
        List<SegmentResp.Snippet> data = new ArrayList<>();
        data.add(new SegmentResp.Snippet("-1", "loading segments...", null));
        return new SegmentResp("", data);
    }

    private Observable<List<ViewModel>> getLoadingItems() {
        List<ViewModel> result = new ArrayList<>();
        int count;
        if (nextPage == 0)
            count = 4;
        else
            count = 2;
        if (layoutType.get() == LAYOUT_TYPE_ROW)
            result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        else
            result.addAll(Collections.nCopies(count, new TileShimmerItemViewModel()));
        return Observable.just(result);
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_CHOOSE_FILTER) {
            if (data != null) {
                filterData = (FilterData) data.getSerializableExtra(Constants.classFilterData);
                this.categoryFilterMap = (HashMap<String, Integer>) data.getSerializableExtra(Constants.categoryFilterMap);
                this.segmentsFilterMap = (HashMap<String, Integer>) data.getSerializableExtra(Constants.segmentsFilterMap);
                this.cityFilterMap = (HashMap<String, String>) data.getSerializableExtra(Constants.cityFilterMap);
                this.localityFilterMap = (HashMap<String, String>) data.getSerializableExtra(Constants.localityFilterMap);
                this.communityFilterMap = (HashMap<String, Integer>) data.getSerializableExtra(Constants.communityFilterMap);
                this.classTypeFilterMap = (HashMap<String, Integer>) data.getSerializableExtra(Constants.classTypeFilterMap);
                this.classScheduleFilterMap = (HashMap<String, Integer>) data.getSerializableExtra(Constants.classScheduleFilterMap);
                this.vendorListFilterMap = (HashMap<String, String>) data.getSerializableExtra(Constants.vendorListFilterMap);
                this.origin = data.getStringExtra(Constants.origin);
                if (origin.equals(ORIGIN_CATALOG))


                    if (categoryFilterMap.isEmpty()) {
                        segmentsVisibility.set(false);
                    } else {
                        if (!segmentsVisibility.get()) {
                            segmentsFilterMap.clear();
                            segmentAvailable = false;
                        }
                    }
                reset();
                       /*  String startDate = data.getStringExtra("startDate");
                String endDate = data.getStringExtra("endDate");
                String keywords = data.getStringExtra("keywords");
                this.startDate = startDate;
                this.endDate = endDate;
                this.keywords = keywords;
                this.communityFilterMap = communityFilterMap;
                this.classTypeFilterMap = classTypeFilterMap;
                this.classScheduleFilterMap = classScheduleFilterMap;
                this.vendorListFilterMap = vendorListFilterMap;
                this.cityFilterMap = cityFilterMap;
                this.localityFilterMap = localityFilterMap;
               filterData.setCommunityId(this.communityFilterMap.isEmpty() ? "" : this.communityFilterMap.values().iterator().next() + "");
                filterData.setClassType(this.classTypeFilterMap.isEmpty() ? "" : this.classTypeFilterMap.values().iterator().next() + "");
                filterData.setClassSchedule(this.classScheduleFilterMap.isEmpty() ? "" : this.classScheduleFilterMap.values().iterator().next() + "");
                filterData.setClassProvider(this.vendorListFilterMap.isEmpty() ? "" : this.vendorListFilterMap.values().iterator().next() + "");
                filterData.setCity(this.cityFilterMap.isEmpty() ? "" : this.cityFilterMap.values().iterator().next() + "");
                filterData.setLocationId(this.localityFilterMap.isEmpty() ? "" : this.localityFilterMap.values().iterator().next() + "");
                filterData.setCategoryId(this.categoryFilterMap.isEmpty() ? "" : this.categoryFilterMap.values().iterator().next() + "");
                filterData.setSegmentId(this.segmentsFilterMap.isEmpty() ? "" : this.segmentsFilterMap.values().iterator().next() + "");
                filterData.setKeywords(this.keywords);
                filterData.setStartDate(this.startDate);
                filterData.setEndDate(this.endDate);*/


//                if (cityFilterMap.isEmpty()) {
//                    this.cityFilterMap.clear();
//                } else {
//                }
//                boolean clearFlag = data.getBooleanExtra("clearFlag", false);
//                if (clearFlag) {
//                    categoryFilterMap.clear();
//                    segmentsFilterMap.clear();
//                    cityFilterMap.clear();
//                    localityFilterMap.clear();
//                    communityFilterMap.clear();
//                    classTypeFilterMap.clear();
//                    classScheduleFilterMap.clear();
//                    vendorListFilterMap.clear();
//                    startDate = "";
//                    endDate = "";
//                    keywords = "";
//                }
//                if (!categoryFilterMap.isEmpty()) {
//                    filterData.setCategoryId(categoryFilterMap.values().iterator().next() + "");
//                    filterData.setSegmentId("");
//                    segmentsVisibility.set(false);
//                }
//                if (!segmentsFilterMap.isEmpty()) {
//                    filterData.setSegmentId(segmentsFilterMap.values().iterator().next() + "");
//                }
//                if (!cityFilterMap.isEmpty()) {
//                    filterData.setCity(cityFilterMap.values().iterator().next() + "");
//                }
//                if (!localityFilterMap.isEmpty()) {
//                    filterData.setLocationId(localityFilterMap.values().iterator().next() + "");
//                }
//                if (!communityFilterMap.isEmpty()) {
//                    filterData.setCommunityId(communityFilterMap.values().iterator().next() + "");
//                }
//                if (!classTypeFilterMap.isEmpty()) {
//                    filterData.setClassType(classTypeFilterMap.values().iterator().next() + "");
//                }
//                if (!classScheduleFilterMap.isEmpty()) {
//                    filterData.setClassSchedule(classScheduleFilterMap.values().iterator().next() + "");
//                }
//                if (!vendorListFilterMap.isEmpty()) {
//                    filterData.setClassProvider(vendorListFilterMap.values().iterator().next() + "");
//                }
//                filterData.setKeywords(keywords);
//                filterData.setStartDate(startDate);
//                filterData.setEndDate(endDate);
            }

        }
    }

    public boolean searchBarInput(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            filterData.setKeywords(v.getText().toString());
            hideSearchBar.set(true);
            navigator.hideKeyBoard(v);
            reset();

        }
        return false;

    }

    private void reset() {
        nonReactiveItems = new ArrayList<>();
        paginationInProgress = true;
        nextPage = 0;
        currentPage = -1;
        callAgain.set(callAgain.get() + 1);
    }

    @Override
    public void retry() {
        connectivityViewmodel.isConnected.set(true);
        callAgain.set(callAgain.get() + 1);
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


}

  /*  private void initClassItemObserver() {
        classes = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems().mergeWith(apiService.generalFilter(filterData.getFilterReq(), 0).map(classDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return nonReactiveItems;
                    }
                }));
            }
        }).doOnNext(new Consumer<List<ViewModel>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<ViewModel> viewModels) throws Exception {
                if (viewModels.size() < 2 && nextPage == 0) {
                    layoutType.set(LAYOUT_TYPE_ROW);
                    UiHelper.changeLayout(layoutType.get());
                }
                nonReactiveItems = viewModels;
                UiHelper.notifyDataChanged();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d("Class Fetch error", "accept: " + throwable.getMessage());
            }
        });
        classes.subscribe();
    }

    private void initClassItemObserverPagination() {
        paginationInProgress = true;
        classes = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems().mergeWith(apiService.generalFilter(filterData.getFilterReq(), nextPage).map(classDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return nonReactiveItems;
                    }
                }));
            }
        }).doOnNext(new Consumer<List<ViewModel>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<ViewModel> viewModels) throws Exception {

                if (viewModels.size() > 0 && viewModels.get(0) instanceof ClassItemViewModel) {
                    Iterator<ViewModel> iter = nonReactiveItems.iterator();
                    while (iter.hasNext()) {
                        if (iter.next() instanceof ShimmerItemViewModel) {
                            iter.remove();
                        }
                    }
                    paginationInProgress = false;
                }
                nonReactiveItems.addAll(viewModels);
                UiHelper.notifyDataChanged();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d("Class Fetch error", "accept: " + throwable.getMessage());
            }
        });
        classes.subscribe();
    }*/
