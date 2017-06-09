package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.SearchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

public class ConnectHomeViewModel extends ViewModel {

    //    public GroupDataViewModel groupVm;
    public  GroupDataViewModel catSegVm;

    public final ObservableField<String> profileImage = new ObservableField();
    public final ObservableField<String> userName = new ObservableField("Hello Learner!");
    public final ObservableField<String> userEmail = new ObservableField("Sign In.");
    public final ObservableField<String> searchQuery = new ObservableField();

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

        onSearchClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(SearchActivity.class, null);
            }
        };
        onFilterClicked = new Action() {
            @Override
            public void run() throws Exception {
                helperFactory.createDialogHelper().showCustomView(R.layout.content_group_data, catSegVm);
            }
        };
        onPostClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (!loggedIn.get()) {
                    Bundle data =new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("To create new content, you need to log in",data);
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


}
