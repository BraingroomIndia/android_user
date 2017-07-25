package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.NotificationCountResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.SearchActivity;

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

public class ConnectHomeViewModel extends ViewModel {

    //    public GroupDataViewModel groupVm;
    public GroupDataViewModel catSegVm;

    public final ObservableField<String> profileImage = new ObservableField();
    public final ObservableField<String> userName = new ObservableField("Hello Learner!");
    public final ObservableField<String> userEmail = new ObservableField("Sign In.");
    public final ObservableField<String> searchQuery = new ObservableField("");
    private String lastSearchQuery="";


    private Disposable notificationDisposable;

    //    public final Observable<List<ViewModel>> feedItems;
//    public final Function<ConnectFeedResp, List<ViewModel>> feedDataMapFunction;
    public final Action onSearchClicked, onFilterClicked, onPostClicked;

    public final ConnectUiHelper uiHelper;
//    public final ConnectFilterData filterData;

    public ConnectHomeViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final HelperFactory helperFactory, @NonNull final ConnectUiHelper uiHelper) {
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
//        this.filterData = new ConnectFilterData();
//        feedDataMapFunction = new Function<ConnectFeedResp, List<ViewModel>>() {
//            @Override
//            public List<ViewModel> apply(ConnectFeedResp resp) throws Exception {
//                List<ViewModel> results = new ArrayList<>();
//                if (resp.getData().size() == 0) {
//                    results.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No classes Available", null));
//                } else {
//                    for (final ConnectFeedResp.Snippet elem : resp.getData()) {
//                        results.add(new ConnectFeedItemViewModel(elem, uiHelper, helperFactory, messageHelper, navigator));
//                    }
//                }
//                return results;
//            }
//        };
//        feedItems = getLoadingItems(4).mergeWith(apiService.getConnectFeed(filterData, 0).map(feedDataMapFunction));
//        catSegVm = new GroupDataViewModel(messageHelper, navigator);
        FieldUtils.toObservable(callAgain).debounce(200, TimeUnit.MILLISECONDS).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return loggedIn.get();
            }
        }).flatMap(new Function<Integer, Observable<NotificationCountResp>>() {
            @Override
            public Observable<NotificationCountResp> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getUnreadMessageCount();
            }
        }).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws Exception {
                if (resp != null && resp.getData() != null) {
                    messageCount = resp.getData().get(0).getCount();

                }
            }
        });

        FieldUtils.toObservable(callAgain).debounce(200, TimeUnit.MILLISECONDS).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return loggedIn.get();
            }
        }).flatMap(new Function<Integer, Observable<NotificationCountResp>>() {
            @Override
            public Observable<NotificationCountResp> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getUnreadNotificationCount();
            }
        }).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws Exception {
                if (resp != null && resp.getData() != null) {
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
                lastSearchQuery=searchQuery;
                uiHelper.setSearchQuery(searchQuery);
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
                if (!loggedIn.get()) {
                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("To create new content, you need to log in", data);
                    return;
                }
                uiHelper.openConnectPost();
            }
        };


    }

    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void retry() {
        connectivityViewmodel.isConnected.set(true);
        uiHelper.retry();
    }

    public void notificationResume() {
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

}
