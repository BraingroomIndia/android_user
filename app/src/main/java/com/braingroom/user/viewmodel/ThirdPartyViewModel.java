package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.ThirdPartyProfileResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.MessagesThreadActivity;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.view.adapters.ViewProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;

public class ThirdPartyViewModel extends ViewModel {

    public Observable<List<ViewModel>> classItems;
    public final ConnectivityViewModel connectivityViewmodel;
    public final DataItemViewModel name = new DataItemViewModel("");
    public final DataItemViewModel postCount = new DataItemViewModel("...");
    public final DataItemViewModel followerCount = new DataItemViewModel("...");
    public final DataItemViewModel followingCount = new DataItemViewModel("...");
    public final DataItemViewModel educationInfo1 = new DataItemViewModel("");
    public final DataItemViewModel educationInfo2 = new DataItemViewModel("");
    public final DataItemViewModel interest = new DataItemViewModel("");
    public final FollowButtonViewModel followButtonVm;

    public final PublishSubject<List<IconTextItemViewModel>> profileDetailsListVms = PublishSubject.create();
    public List<IconTextItemViewModel> dataList;
    public final ImageUploadViewModel imageUploadVm;

    public final String userId;


    @Getter
    public ConnectFilterData connectFilterData;
    public Observable<List<ViewModel>> feedItems;
    private final Function<ConnectFeedResp, List<ViewModel>> feedDataMapFunction;
    private boolean paginationInProgress = false;
    private int nextPage = 0;
    private int currentPage = -1;

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

    public final Action onMessageClicked;

    public ThirdPartyViewModel(@NonNull String userId, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final HelperFactory helperFactory,
                               @NonNull final ConnectUiHelper uiHelper) {
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        followButtonVm = new FollowButtonViewModel(userId, messageHelper, navigator, FollowButtonViewModel.STATE_LOADING);
        imageUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.avatar_male, null);

        this.userId = userId;
        //Connect Part
        connectFilterData = new ConnectFilterData();
        connectFilterData.setAuthorId(userId);
        connectFilterData.setMajorCateg(ConnectHomeActivity.LEARNER_FORUM);
        connectFilterData.setMinorCateg(ConnectHomeActivity.TIPS_TRICKS);

        onMessageClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn()) {
                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ThirdPartyViewActivity.class.getSimpleName());
                    data.putString("thirdPartyUserId", pref.getString(Constants.BG_ID, ""));
                    messageHelper.showLoginRequireDialog("Only logged in users can send a message", data);
                }

                String userName = name.s_1.get();
                Bundle bundle = new Bundle();
                bundle.putString("sender_id", pref.getString(Constants.BG_ID, ""));
                bundle.putString("sender_name", userName);
                navigator.navigateActivity(MessagesThreadActivity.class, bundle);

            }
        };

        apiService.getThirdPartyProfile(userId).subscribe(new Consumer<ThirdPartyProfileResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ThirdPartyProfileResp resp) throws Exception {
                ThirdPartyProfileResp.Snippet data = resp.getData().get(0);
                name.s_1.set(data.getName());
                educationInfo1.s_1.set(data.getEduInfo1());
                educationInfo2.s_1.set(data.getEduInfo2());
                interest.s_1.set(data.getInterest());
                postCount.s_1.set("" + data.getPost_count());
                followerCount.s_1.set("" + data.getFollower_count());
                followingCount.s_1.set("" + data.getFollowing_count());
                if (data.getFollowStatus() == 0)
                    followButtonVm.changeButtonState(FollowButtonViewModel.STATE_FOLLOW);
                else followButtonVm.changeButtonState(FollowButtonViewModel.STATE_FOLLOWED);
                dataList = new ArrayList<>();
                addProfileData(R.drawable.ic_domain_black_24dp, data.getEduInfo1(), dataList);
                addProfileData(R.drawable.ic_school_black_24dp, data.getEduInfo2(), dataList);
                addProfileData(R.drawable.ic_domain_black_24dp, data.getInterest(), dataList);
                profileDetailsListVms.onNext(dataList);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.e("ThirdPartyProfile", "accept: ", throwable);
            }
        });


        feedDataMapFunction = new Function<ConnectFeedResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(ConnectFeedResp resp) throws Exception {
                currentPage = nextPage;
                nextPage = resp.getNextPage();
                if (resp.getData().size() == 0 && nextPage < 1) {
                    nonReactiveItems.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No Post Available", null));
                } else {
                    //  Log.d("ConnectFeed", "\napply: nextPage:\t " + nextPage + "\n currentPage:\t" + currentPage);
                    for (final ConnectFeedResp.Snippet elem : resp.getData()) {
                        nonReactiveItems.add(new ConnectFeedItemViewModel(elem, uiHelper, helperFactory, messageHelper, navigator));
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
                return apiService.getConnectFeed(connectFilterData, nextPage)
                        .map(feedDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                return nonReactiveItems;
                            }
                        }).mergeWith(getLoadingItems());
            }
        });


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
    public void paginate() {
        if (nextPage > -1 && !paginationInProgress) {
            nextPage = nextPage + 1;
            callAgain.set(callAgain.get() + 1);

        }
    }

    private void addProfileData(int icon, String name, List<IconTextItemViewModel> dataList) {
        if (!"".equals(name))
            dataList.add(new IconTextItemViewModel(icon, name, null));
    }


    public void rest() {
        nonReactiveItems = new ArrayList<>();
        nextPage = 0;
        currentPage = -1;
        paginationInProgress = false;
        callAgain.set(callAgain.get() + 1);
    }

    public void setFilterData(ConnectFilterData connectFilterData) {
        this.connectFilterData = connectFilterData;
        rest();
    }

    private Observable<List<ViewModel>> getLoadingItems() {
        int count;
        if (nonReactiveItems.isEmpty())
            count = 4;
        else
            count = 2;
        List<ViewModel> result = new ArrayList<>();
        result.addAll(nonReactiveItems);
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }

}
