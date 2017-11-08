package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.NotificationCountResp;
import com.braingroom.user.model.response.PrimeMessageResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.FeaturedPostActivity;
import com.braingroom.user.view.activity.SearchActivity;
import com.braingroom.user.view.adapters.ViewProvider;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import lombok.Getter;

public class ConnectHomeViewModel extends ViewModel {

    //    public GroupDataViewModel groupVm;
    public GroupDataViewModel catSegVm;

    public final ImageUploadViewModel imageUploadViewModel;
    public final ObservableField<String> profileImage = new ObservableField();
    public final ObservableField<String> userName = new ObservableField("Hello Learner!");
    public final ObservableField<String> userEmail = new ObservableField("Sign In.");
    public final ObservableField<String> searchQuery = new ObservableField("");
    private String lastSearchQuery = "";
    private ObservableBoolean loggedIn;
    private Disposable notificationDisposable;

    public Observable<List<ViewModel>> feedItems;
    private final Function<ConnectFeedResp, List<ViewModel>> feedDataMapFunction;
    @Getter
    private ConnectFilterData filterData;
    private boolean paginationInProgress = false;
    private int nextPage = 0;
    private int currentPage = -1;


    //    public final Observable<List<ViewModel>> feedItems;
//    public final Function<ConnectFeedResp, List<ViewModel>> feedDataMapFunction;
    public final Action onSearchClicked, onFilterClicked, onPostClicked, onPostOftheDayClicked, onPrimeContentClicked;

    public final ConnectUiHelper uiHelper;

    private String primeTimeMessage;
//    public final ConnectFilterData filterData;

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

    public ConnectHomeViewModel(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final ConnectFilterData connectFilterData,
                                @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                                @NonNull final HelperFactory helperFactory, @NonNull final ConnectUiHelper uiHelper) {
        imageUploadViewModel = new ImageUploadViewModel(R.drawable.avatar_male, pref.getString(Constants.PROFILE_PIC, ""));
        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.filterData = connectFilterData;
        this.loggedIn = new ObservableBoolean(getLoggedIn());
        this.profileImage.set(pref.getString(Constants.PROFILE_PIC, null));
        this.userName.set(pref.getString(Constants.NAME, "Hello Learner!"));
        this.userEmail.set(pref.getString(Constants.EMAIL, null));
        this.uiHelper = uiHelper;
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        //New Changes
        this.filterData.setCountryId(pref.getInt("connect_country_id", 0) == 0 ? "" : pref.getInt("connect_country_id", 0) + "");
        this.filterData.setStateId(pref.getInt("connect_state_id", 0) == 0 ? "" : pref.getInt("connect_state_id", 0) + "");
        this.filterData.setCityId(pref.getInt("connect_city_id", 0) == 0 ? "" : pref.getInt("connect_city_id", 0) + "");
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        if (callAgain == null)
            callAgain = new ObservableField<>(0);
        if (callAgain.get() == null)
            callAgain.set(0);
        feedDataMapFunction = new Function<ConnectFeedResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(ConnectFeedResp resp) throws Exception {
                currentPage = nextPage;
                nextPage = resp.getNextPage();
                if (currentPage < 1)
                    setScreenName("Connect Home Page " + filterData.getMinorCateg());
                if (resp.getData().size() == 0 && currentPage < 1) {
                    nonReactiveItems.add(new EmptyItemViewModel(R.drawable.ic_no_post_64dp, null, "No Post Available", null));
                } else {
                    //  Log.d("ConnectFeed", "\napply: nextPage:\t " + nextPage + "\n currentPage:\t" + currentPage);
                    for (final ConnectFeedResp.Snippet elem : resp.getData()) {
                        nonReactiveItems.add(new ConnectFeedItemViewModel(elem, false, false, uiHelper, helperFactory, messageHelper, navigator));
                    }
                }

                paginationInProgress = false;
                return nonReactiveItems;
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
                return apiService.getConnectFeed(filterData, nextPage)
                        .map(feedDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                return getLoadingItems();
                            }
                        }).mergeWith(Observable.just(getLoadingItems()));
            }
        });
        //New Changes
//        this.filterData = new ConnectFilterData();
//        feedDataMapFunction = new Function<ConnectFeedResp, List<ViewModel>>() {
//            @Override
//            public List<ViewModel> apply(ConnectFeedResp resp) throws Exception {
//                List<ViewModel> results = new ArrayList<>();
//                if (resp.getData().size() == 0) {
//                    results.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No classes Available", null));
//                } else {
//                    for (final ConnectFeedResp.Snippet elem : resp.getData()) {
//                        results.add(new ConnectFeedItemViewModel(elem, UiHelper, helperFactory, messageHelper, navigator));
//                    }
//                }
//                return results;
//            }
//        };
//        feedItems = getLoadingItems(4).mergeWith(apiService.getConnectFeed(filterData, 0).map(feedDataMapFunction));
//        catSegVm = new GroupDataViewModel(messageHelper, navigator);
        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoggedIn();
            }
        }).flatMap(new Function<Integer, Observable<NotificationCountResp>>() {
            @Override
            public Observable<NotificationCountResp> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                apiService.getPrimeTimeMessage().subscribe(new Consumer<PrimeMessageResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull PrimeMessageResp resp) throws Exception {
                        if (!isEmpty(resp) && !isEmpty(resp.getData()) && !isEmpty(resp.getData().get(0)))
                            primeTimeMessage = resp.getData().get(0).getValue();
                    }
                });
                return apiService.getUnreadMessageCount();
            }
        }).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws Exception {
                if (resp != null && resp.getData() != null && !resp.getData().isEmpty()) {
                    messageCount = resp.getData().get(0).getCount();
                    uiHelper.setCount(notificationCount, messageCount);

                }
            }
        });

        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoggedIn();
            }
        }).flatMap(new Function<Integer, Observable<NotificationCountResp>>() {
            @Override
            public Observable<NotificationCountResp> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getUnreadNotificationCount();
            }
        }).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws Exception {
                if (resp != null && resp.getData() != null && !resp.getData().isEmpty()) {
                    notificationCount = resp.getData().get(0).getCount();
                    uiHelper.setCount(notificationCount, messageCount);

                }
            }
        });

        FieldUtils.toObservable(searchQuery)
                .debounce(200, TimeUnit.MILLISECONDS).filter(new Predicate<String>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull String searchQuery) throws Exception {
                return !(("").equals(searchQuery) && ("").equals(lastSearchQuery));
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull String searchQuery) throws Exception {
                lastSearchQuery = searchQuery.trim();
                filterData.setSearchQuery(searchQuery.trim());
                rest();
            }
        });


        onSearchClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(SearchActivity.class, null);
            }
        };
        onFilterClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openFilter();
            }
        };
        onPostClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn()) {
                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("To create new content, you need to log in", data);
                    return;
                }
                uiHelper.openConnectPost();
            }
        };
        onPostOftheDayClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(FeaturedPostActivity.class, null);
            }
        };
        onPrimeContentClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (isEmpty(primeTimeMessage)) {
                    messageHelper.showProgressDialog("Wait", "loading");
                    apiService.getPrimeTimeMessage().subscribe(new Consumer<PrimeMessageResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull PrimeMessageResp resp) throws Exception {
                            if (!isEmpty(resp) && !isEmpty(resp.getData()) && !isEmpty(resp.getData().get(0)))
                                primeTimeMessage = resp.getData().get(0).getValue();
                            messageHelper.showDismissInfo("Prime Time", "Close", primeTimeMessage);
                        }
                    });
                } else
                    messageHelper.showDismissInfo("Prime Time", "Close", primeTimeMessage);


            }
        };


    }

    private List<ViewModel> getLoadingItems() {
        int count;
        if (nonReactiveItems.isEmpty())
            count = 4;
        else
            count = 2;
        List<ViewModel> result = new ArrayList<>();
        result.addAll(nonReactiveItems);
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return (result);
    }

    @Override
    public void onResume() {
        super.onResume();
        loggedIn.set(getLoggedIn());
        this.profileImage.set(pref.getString(Constants.PROFILE_PIC, null));
        imageUploadViewModel.setRemoteAddress(pref.getString(Constants.PROFILE_PIC, ""));
        this.userName.set(pref.getString(Constants.NAME, "Hello Learner!"));
        this.userEmail.set(pref.getString(Constants.EMAIL, null));
        notificationResume();
        connectivityViewmodel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        safelyDispose(notificationDisposable);
        connectivityViewmodel.onPause();
    }

    @Override
    public void paginate() {
        if (nextPage > -1 && !paginationInProgress) {
            callAgain.set(callAgain.get() + 1);

        }
    }

    @Override
    public void retry() {
        connectivityViewmodel.isConnected.set(true);
        callAgain.set(callAgain.get() + 1);
    }

    private void notificationResume() {
        notificationDisposable = UserApplication.getInstance().getNewNotificationBus().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isNewNotification) {
                if (isNewNotification)
                    callAgain.set(callAgain.get() + 1);

                Log.d("Notification", "accept: " + isNewNotification);
            }
        });
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    public void setFilterData(ConnectFilterData connectFilterData) {
        if (filterData.hashCode() == connectFilterData.hashCode())
            return;
        this.filterData = connectFilterData;
        rest();
    }

    public void rest() {
        nonReactiveItems = new ArrayList<>();
        nextPage = 0;
        currentPage = -1;
        paginationInProgress = false;
        callAgain.set(callAgain.get() + 1);
    }

}
