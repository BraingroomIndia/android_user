package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.model.response.LikedUsersListResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.FollowButtonViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by godara on 26/08/17.
 */

public class FollowersUserViewModel extends ViewModel {
    public final Action onBackClicked;
    public final Observable<List<ViewModel>> items;

    public FollowersUserViewModel(final ConnectUiHelper uiHelper, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator
            , @NonNull final HelperFactory helperFactory, String userId) {
        if (TextUtils.isEmpty(userId))
            userId = pref.getString(Constants.BG_ID, "");
        items = apiService.getFollowers(userId).map(new Function<LikedUsersListResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(LikedUsersListResp resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                for (final LikedUsersListResp.Snippet elem : resp.getData()) {
                    results.add(new FollowItemViewModel(elem.getUserImage(), elem.getUserName(), elem.getUserId()
                            , messageHelper, navigator, elem.getFollowStatus()));
                }
                return results;
            }
        });
        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.popFragment();
//                navigator.launchPlaceSearchIntent(PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        };
    }
}
