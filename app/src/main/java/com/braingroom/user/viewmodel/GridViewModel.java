package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.model.response.ConnectSectionResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.ClassType;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.CommunityListActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.SegmentListActivity;
import com.braingroom.user.view.adapters.CustomGridLayoutManger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;


public class GridViewModel extends ViewModel {
    protected final static int CATEGORY = 1;
    protected final static int COMMUNITY = 2;
    protected final static int SEGMENT = 3;
    protected final static int CONNECT = 4;
    protected final static int OnlineCommunity = 5;


    private String temp1 = "https://www.braingroom.com/img/community.jpg";
    private String temp2 = "https://www.braingroom.com/img/online_classes.jpg";


    public final Observable<List<ViewModel>> gridItems;
    public final ObservableField<String> title = new ObservableField<>("");
    public final HashMap<String, Integer> categoryMap;
    public final Navigator navigator;
    public final int type;
    public final CustomGridLayoutManger layout;

    public GridViewModel(@NonNull Navigator navigator, int type, @Nullable final HashMap<String, Integer> categoryMap1) {
        this.navigator = navigator;
        this.type = type;
        this.categoryMap = categoryMap1 != null ? categoryMap1 : new HashMap<String, Integer>();
        gridItems = FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getGridItems(categoryMap);
            }
        });
        layout = null;
    }

    public GridViewModel(@NonNull Navigator navigator, int type, @Nullable final HashMap<String, Integer> categoryMap1, CustomGridLayoutManger layout) {
        this.navigator = navigator;
        this.type = type;
        this.categoryMap = categoryMap1 != null ? categoryMap1 : new HashMap<String, Integer>();
        gridItems = FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getGridItems(categoryMap);
            }
        });
        this.layout = layout;
    }

    private Observable<List<ViewModel>> getGridItems(@NonNull HashMap<String, Integer> categoryMap) {
        final String categoryTitle = "Learn new skills or pick up a new hobby";
        final String segmentTitle = "Segments";
        final String connectSection = "Connect Sections";
        switch (type) {
            case CATEGORY:
                title.set(categoryTitle);
                return getCategory().mergeWith(Observable.just(getGridLoadingItems(6)));
            case COMMUNITY:
                getCommunity().mergeWith(Observable.just(getGridLoadingItems(9)));
                break;
            case SEGMENT:
                title.set(segmentTitle);
                if (!categoryMap.isEmpty())
                    return getSegment(categoryMap).mergeWith(Observable.just(getGridLoadingItems(6)));
                else return Observable.just(getGridLoadingItems(6));
            case CONNECT:
                title.set(connectSection);
                return getConnectSection().mergeWith(Observable.just(getGridLoadingItems(5)));
            case OnlineCommunity:
                title.set("Group classes and Online classes");
                return getCommunityAndOnlineClass();
            default:
                return Observable.just(getGridLoadingItems(6));

        }
        return Observable.just(getGridLoadingItems(6));
    }

    private Observable<List<ViewModel>> getCategory() {
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
                                    if (snippet.getId() != -1) {
                                        Bundle data = new Bundle();
                                        HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();
                                        categoryMap.put(snippet.getCategoryName(), snippet.getId());
                                        data.putSerializable(Constants.categoryFilterMap, categoryMap);
                                        navigator.navigateActivity(SegmentListActivity.class, data);
                                    }
                                }
                            }));
                }
                return results;
            }
        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return getGridLoadingItems(6);
            }
        });
    }

    private Observable<List<ViewModel>> getSegment(@NonNull final HashMap<String, Integer> categoryMap) {
        Map.Entry<String, Integer> entry = categoryMap.entrySet().iterator().next();
        final String categoryId = entry.getValue() + "";
        final String categoryName = entry.getKey();
        return apiService.getSegments(categoryId).map(new Function<SegmentResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(SegmentResp resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();

                for (final SegmentResp.Snippet elem : resp.getData()) {
                    if (elem.getId() != -1) {
                        apiSuccessful = true;
                        if (layout != null)
                            layout.setTotalCount(resp.getData().size());
                        results.add(new IconTextItemViewModel(elem.getSegmentImage(), elem.getSegmentName(),
                                new MyConsumer<IconTextItemViewModel>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                        {
                                            Bundle data = new Bundle();
                                            FilterData filterData = new FilterData();
                                            HashMap<String, Integer> segmentFilterMap = new HashMap<>();
                                            segmentFilterMap.put(elem.getSegmentName(), elem.getId());
                                            filterData.setCategoryFilterMap(categoryMap);
                                            filterData.setSegmentsFilterMap(segmentFilterMap);
                                            data.putSerializable(Constants.classFilterData, filterData);
                                            data.putString(Constants.origin, FilterViewModel.ORIGIN_HOME);
                                            navigator.navigateActivity(ClassListActivity.class, data);
                                        }

                                    }
                                }
                        ));


                    }

                }
                return results;
            }
        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return getGridLoadingItems(6);
            }
        });

    }

    private Observable<List<ViewModel>> getCommunity() {
        return apiService.getCommunity()
                .map(new Function<CommunityResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(CommunityResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.getData().size() == 0) resp = getDefaultResponse();
                        for (final CommunityResp.Snippet snippet : resp.getData()) {
                            if (snippet.getImage() != null)
                                snippet.setImage(snippet.getImage().replaceAll("jpg", "png"));
                            results.add
                                    (new IconTextItemViewModel(snippet.getImage(), snippet.getName(), new MyConsumer<IconTextItemViewModel>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                            if (!snippet.getId().equals("-1")) {
                                                apiSuccessful = true;
                                                Bundle data = new Bundle();
                                                FilterData filterData = new FilterData();
                                                HashMap<String, Integer> communityFilterMap = new HashMap<String, Integer>();
                                                communityFilterMap.put(snippet.getName(), Integer.parseInt(snippet.getId()));
                                                filterData.setCommunityFilterMap(communityFilterMap);
                                                data.putSerializable(Constants.classFilterData, filterData);
                                                data.putString(Constants.origin, FilterViewModel.ORIGIN_COMMUNITY);
                                                navigator.navigateActivity(ClassListActivity.class, data);
                                            }
                                        }
                                    }));
                        }
                        return results;
                    }
                }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return getGridLoadingItems(9);
                    }
                });
    }

    private Observable<List<ViewModel>> getConnectSection() {
        return apiService.getConnectSections().observeOn(AndroidSchedulers.mainThread()).map(new Function<ConnectSectionResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull ConnectSectionResp resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                if (resp.getData().isEmpty()) return getGridLoadingItems(5);
                for (final ConnectSectionResp.Snippet snippet : resp.getData()) {
                    results.add(new IconTextItemViewModel(snippet.getImgUrl(), snippet.getName(), new MyConsumer<IconTextItemViewModel>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                            Bundle data = new Bundle();
                            ConnectFilterData connectFilterData = new ConnectFilterData();
                            connectFilterData.setMajorCateg(snippet.getMajorCategory());
                            connectFilterData.setMinorCateg(snippet.getMinorCategory());
                            data.putSerializable("connectFilterData", connectFilterData);
                            navigator.navigateActivity(ConnectHomeActivity.class, data);
                        }
                    }));
                }
                return results;
            }
        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return getGridLoadingItems(5);
            }
        });
    }

    private Observable<List<ViewModel>> getCommunityAndOnlineClass() {
        ViewModel community, onlineClass;
        List<ViewModel> list = new ArrayList<ViewModel>();
        community = new IconTextItemViewModel(temp1, "Group Classes", new MyConsumer<IconTextItemViewModel>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                navigator.navigateActivity(CommunityListActivity.class, null);
            }
        });
        onlineClass = new IconTextItemViewModel(temp2, "Online Classes", new MyConsumer<IconTextItemViewModel>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                FilterData filterData = new FilterData();
                Bundle data = new Bundle();
                HashMap<String, Integer> classTypeFilterMap = new HashMap<>();
                classTypeFilterMap.put(ClassType.SEMINAR.name, ClassType.SEMINAR.id);
                filterData.setClassTypeFilterMap(classTypeFilterMap);
                data.putSerializable(Constants.classFilterData, filterData);
                data.putString(Constants.origin, FilterViewModel.ORIGIN_HOME);
                navigator.navigateActivity(ClassListActivity.class, data);
            }
        });
        list.add(community);
        list.add(onlineClass);
        return Observable.just(list);
    }

    private CommunityResp getDefaultResponse() {
        return new CommunityResp(Collections.nCopies(9, new CommunityResp.Snippet("-1", "", null)));
    }

    private List<ViewModel> getGridLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new IconTextShimmerItemViewModel()));
        return result;
    }

}


;/*FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return  Observable.just(getDefaultResponse()).mergeWith(apiService.getCommunity())
                        .map(new Function<CommunityResp, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(CommunityResp resp) throws Exception {
                                results = new ArrayList<>();
                                if (resp.getData().size() == 0) resp = getDefaultResponse();
                                for (final CommunityResp.Snippet snippet : resp.getData()) {
                                    if (snippet.getImage()!=null)
                                        snippet.setImage(snippet.getImage().replaceAll("jpg","png"));
                                    results.add
                                            (new IconTextItemViewModel(snippet.getImage(), snippet.getName(), new MyConsumer<IconTextItemViewModel>() {
                                                @Override
                                                public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                                    if (!snippet.getId().equals("-1")) {
                                                        apiSuccessful=true;
                                                        Bundle data = new Bundle();
                                                        FilterData filterData = new FilterData();
                                                        HashMap<String, Integer> communityFilterMap = new HashMap<String, Integer>();
                                                        communityFilterMap.put(snippet.getName(),Integer.parseInt(snippet.getId()));
                                                        filterData.setCommunityId(snippet.getId());
                                                        data.putSerializable("community",communityFilterMap);
                                                        data.putSerializable("filterData", filterData);
                                                        data.putString("origin", FilterViewModel.ORIGIN_COMMUNITY);
                                                        navigator.navigateActivity(destination, data);
                                                    }
                                                }
                                            }));
                                }
                                return results;
                            }
                        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                return results;
                            }
                        });
            }
        });*/
