package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.OnlineClassVideoActivity;
import com.braingroom.user.view.activity.ClassDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

/**
 * Created by android on 26/03/18.
 */

public class OnlineClassSimpleListViewModel extends ViewModel {
    private final String listType;
    private final String userId;
    public Observable<List<ViewModel>> result;
    private boolean paginationInProgress = false;
    private int nextPage = 1;
    private int currentPage = 0;
    private boolean nextPageAvailable = true;
    private Observable<List<ClassData>> apiObservable = null;
    private List<ViewModel> classes;

    public OnlineClassSimpleListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull String listType1, String id) {
        this.listType = listType1;
        classes = new ArrayList<>();
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
                connectivityViewmodel.isConnected.set(true);
                Timber.tag(TAG).d("run: " + callAgain.get());
            }
        });
        if (id != null)
            this.userId = id;
        else
            this.userId = pref.getString(Constants.BG_ID, "");
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        result = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                paginationInProgress = true;
                Timber.tag(TAG).d("apply:" + callAgain.get());
                if ("Wishlist".equalsIgnoreCase(listType))
                    apiObservable = apiService.getWishList(nextPage);
                else if ("onlineBookingHistory".equalsIgnoreCase(listType))
                    apiObservable = apiService.getBookingOnlineHistory(userId, nextPage);
                return apiObservable
                        .map(new Function<List<ClassData>, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                                List<ViewModel> results = new ArrayList<>();
                                if (resp.size() == 0)
                                    nextPageAvailable = false;
                                if (resp.size() == 0 && classes.size() == 0)
                                    classes.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No classes Available", null));

                                for (final ClassData elem : resp) {
                                    if (elem.getClassType().equalsIgnoreCase("Online Classes"))
                                        elem.setLocality("Online");
                                    else if (elem.getClassType().equalsIgnoreCase("Webinars"))
                                        elem.setLocality("Webinar");
                                    classes.add(new OnlineClassItemViewModel(elem, new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            if (!elem.getId().equals("-1")) {
                                                if ("onlineBookingHistory".equalsIgnoreCase(listType)) {
                                                    Bundle data = new Bundle();
                                                    data.putString("txn_id", elem.getTxnId());
                                                    data.putString("class_topic", elem.getClassTopic());
                                                    navigator.navigateActivity(OnlineClassVideoActivity.class, data);

                                                }
                                            }
                                        }
                                    }));
                                }
                                paginationInProgress = false;
                                return classes;
                            }
                        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                return classes;
                            }
                        }).mergeWith(getLoadingItems(3));
            }
        });
    }

    @Override
    public void paginate() {
        if (nextPageAvailable && !paginationInProgress) {
            nextPage = nextPage + 1;
            callAgain.set(callAgain.get() + 1);

        }
    }

    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
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
        callAgain.set(callAgain.get() + 1);
        connectivityViewmodel.isConnected.set(true);
    }
}