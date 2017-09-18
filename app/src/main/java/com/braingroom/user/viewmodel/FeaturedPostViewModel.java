package com.braingroom.user.viewmodel;

import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.WinnerResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.FeaturedPostActivity;
import com.braingroom.user.viewmodel.fragment.FeaturedPostItemViewModel;
import com.braingroom.user.viewmodel.fragment.WinnersViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.braingroom.user.view.activity.ConnectHomeActivity.LEARNER_FORUM;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TIPS_TRICKS;

/**
 * Created by godara on 14/09/17.
 */

public class FeaturedPostViewModel extends ViewModel {

    private boolean apiSuccessful1;

    public List<ConnectFeedItemViewModel> connectFeedItemViewModelList = new ArrayList<>();

    public List<WinnersViewModel> winnersViewModelList = new ArrayList<>();

    public FeaturedPostViewModel(final @NonNull Navigator navigator, @NonNull final HelperFactory helperFactory,
                                 @NonNull final MessageHelper messageHelper, final FeaturedPostActivity.UiHelper uiHelper,
                                 final ConnectUiHelper connectUiHelper) {
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });


        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                ConnectFilterData filterData = new ConnectFilterData();
                filterData.setMajorCateg(LEARNER_FORUM);
                filterData.setMinorCateg(TIPS_TRICKS);
                filterData.setFeaturedPost(true);
                apiService.getConnectFeed(filterData, 0).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        navigator.finishActivity();
                    }
                }).subscribe(new Consumer<ConnectFeedResp>() {
                    @Override
                    public void accept(@NonNull ConnectFeedResp resp) throws Exception {
                        apiSuccessful = true;
                        connectFeedItemViewModelList = new ArrayList<>();
                        for (ConnectFeedResp.Snippet snippet : resp.getData()) {
                            connectFeedItemViewModelList.add(new ConnectFeedItemViewModel(snippet, false, false, connectUiHelper, helperFactory, messageHelper, navigator));
                        }
                        uiHelper.setPostPagerAdapter();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        navigator.finishActivity();
                    }
                });
            }
        });

        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return !apiSuccessful1;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                ConnectFilterData filterData = new ConnectFilterData();
                filterData.setMajorCateg(LEARNER_FORUM);
                filterData.setMinorCateg(TIPS_TRICKS);
                filterData.setFeaturedPost(true);
                apiService.getWeaklyWinners().doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        navigator.finishActivity();
                    }
                }).subscribe(new Consumer<WinnerResp>() {
                    @Override
                    public void accept(@NonNull WinnerResp resp) throws Exception {
                        apiSuccessful1 = true;
                        winnersViewModelList = new ArrayList<>();
                        for (WinnerResp.Snippet snippet : resp.getData()) {
                            winnersViewModelList.add(new WinnersViewModel(snippet, navigator));
                        }
                        uiHelper.setWinnerPagerAdapter();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        navigator.finishActivity();
                    }
                });
            }
        });
    }
}
