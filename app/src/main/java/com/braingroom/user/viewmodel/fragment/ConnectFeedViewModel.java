package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.adapters.ViewProvider;
import com.braingroom.user.view.fragment.ConnectFeedFragment;
import com.braingroom.user.viewmodel.ClassItemViewModel;
import com.braingroom.user.viewmodel.ConnectFeedItemViewModel;
import com.braingroom.user.viewmodel.ConnectivityViewModel;
import com.braingroom.user.viewmodel.EmptyItemViewModel;
import com.braingroom.user.viewmodel.RowShimmerItemViewModel;
import com.braingroom.user.viewmodel.ShimmerItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.IntFunction;
import io.reactivex.functions.Predicate;
import lombok.Getter;

import static com.crashlytics.android.beta.Beta.TAG;

public class ConnectFeedViewModel extends ViewModel {

    public Observable<List<ViewModel>> feedItems;
    public final Function<ConnectFeedResp, List<ViewModel>> feedDataMapFunction;
    public ConnectFilterData filterData;
    private boolean paginationInProgress = false;
    private int nextPage = 0;
    private int currentPage = -1;
    public final ConnectFeedFragment.FragmentUiHelper fragmentUiHelper;

    @Getter
    ViewProvider viewProvider = new ViewProvider() {
        @Override
        public int getView(ViewModel vm) {
            if (vm instanceof ConnectFeedItemViewModel)
                return R.layout.item_connect_feed;
            else if (vm instanceof EmptyItemViewModel)
                return R.layout.item_empty_data;
            else if (vm instanceof RowShimmerItemViewModel)
                return R.layout.item_shimmer_row;
            return 0;
        }
    };

    public ConnectFeedViewModel(String majorCateg, String minorCateg, @NonNull final Navigator navigator,
                                @NonNull final HelperFactory helperFactory, @NonNull final MessageHelper messageHelper, String vendorId
            , @NonNull final ConnectUiHelper uiHelper, final ConnectFeedFragment.FragmentUiHelper fragmentUiHelper) {
        this.filterData = new ConnectFilterData();
        this.filterData.setMajorCateg(majorCateg);
        this.filterData.setMinorCateg(minorCateg);
        this.filterData.setCountryId(pref.getInt("connect_country_id", 0) == 0 ? "" : pref.getInt("connect_country_id", 0) + "");
        this.filterData.setStateId(pref.getInt("connect_state_id", 0) == 0 ? "" : pref.getInt("connect_state_id", 0) + "");
        this.filterData.setCityId(pref.getInt("connect_city_id", 0) == 0 ? "" : pref.getInt("connect_city_id", 0) + "");
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        this.fragmentUiHelper = fragmentUiHelper;
        feedDataMapFunction = new Function<ConnectFeedResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(ConnectFeedResp resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                if (resp.getData().size() == 0) {
                    results.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No classes Available", null));
                } else {
                    currentPage = nextPage;
                    nextPage = resp.getNextPage();
                    Log.d("ConnectFeed", "\napply: nextPage:\t " + nextPage + "\n currentPage:\t" + currentPage);
                    for (final ConnectFeedResp.Snippet elem : resp.getData()) {
                        results.add(new ConnectFeedItemViewModel(elem, uiHelper, helperFactory, messageHelper, navigator));
                    }
                }

                return results;
            }
        };
        feedItems = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                paginationInProgress = true;
                return getLoadingItems().mergeWith(apiService.getConnectFeed(filterData, nextPage)
                        .map(feedDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return new ArrayList<ViewModel>();
                    }
                })).doOnNext(new Consumer<List<ViewModel>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<ViewModel> viewModels) throws Exception {

                        if (viewModels.size() > 0 && viewModels.get(0) instanceof ConnectFeedItemViewModel) {
                            Iterator<ViewModel> iter = nonReactiveItems.iterator();
                            while (iter.hasNext()) {
                                if (iter.next() instanceof RowShimmerItemViewModel) {
                                    iter.remove();
                                }
                            }

                            paginationInProgress = false;
                        }
                        nonReactiveItems.addAll(viewModels);
                        fragmentUiHelper.notifyDataChanged();

                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.d("Class Fetch error", "accept: " + throwable.getMessage());
                    }
                });
            }
        });
        feedItems.subscribe();
        //initConnectItemObserver(filterData);
    }

    public void initConnectItemObserver(ConnectFilterData filterData1) {
        this.filterData = filterData1;
        feedItems = FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems().mergeWith(apiService.getConnectFeed(filterData, 0)
                        .map(feedDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return new ArrayList<>();
                    }
                })).doOnNext(new Consumer<List<ViewModel>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<ViewModel> viewModels) throws Exception {
                        nonReactiveItems = viewModels;
                        fragmentUiHelper.notifyDataChanged();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.d("Class Fetch error", "accept: " + throwable.getMessage());
                    }
                });
            }
        });
        feedItems.subscribe();
    }

    @Override
    public void paginate() {
        if (nextPage > 0 && !paginationInProgress) {
            paginationInProgress = true;
            callAgain.set(callAgain.get() + 1);
        }
    }

    public void reset(@Nullable ConnectFilterData filterData1) {
        if (filterData1 != null)
            this.filterData = filterData1;
        nonReactiveItems = new ArrayList<>();
        paginationInProgress = true;
        nextPage = 0;
        currentPage = -1;
        callAgain.set(callAgain.get() + 1);
    }


    private void initConnectItemObserverPagination() {
        feedItems = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems().mergeWith(apiService.getConnectFeed(filterData, nextPage).map(feedDataMapFunction)
                        .onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return new ArrayList<ViewModel>();
                    }
                })).doOnNext(new Consumer<List<ViewModel>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<ViewModel> viewModels) throws Exception {

                        if (viewModels.size() > 0 && viewModels.get(0) instanceof ConnectFeedItemViewModel) {
                            Iterator<ViewModel> iter = nonReactiveItems.iterator();
                            while (iter.hasNext()) {
                                if (iter.next() instanceof ShimmerItemViewModel) {
                                    iter.remove();
                                }
                            }
                            paginationInProgress = false;
                        }
                        nonReactiveItems.addAll(viewModels);
                        fragmentUiHelper.notifyDataChanged();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.d("Class Fetch error", "accept: " + throwable.getMessage());
                    }
                });
            }
        });
        paginationInProgress = true;
        feedItems.subscribe();
    }

    @Override
    public void retry() {
        callAgain.set(callAgain.get() + 1);
        connectivityViewmodel.isConnected.set(true);
    }


    private Observable<List<ViewModel>> getLoadingItems() {
        int count;
        if (nonReactiveItems.isEmpty())
            count = 4;
        else
            count = 2;
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }


}
