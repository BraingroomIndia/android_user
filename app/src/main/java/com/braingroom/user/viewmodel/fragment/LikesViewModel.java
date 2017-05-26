package com.braingroom.user.viewmodel.fragment;

import com.braingroom.user.model.response.LikedUsersListResp;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class LikesViewModel extends ViewModel {

    public final Action onBackClicked;
    public final Observable<List<ViewModel>> likesVm;

    public LikesViewModel(String postId, String commentId, String replyId, final ConnectUiHelper uiHelper) {

        likesVm = apiService.getLikedUsers(postId)
                .map(new Function<LikedUsersListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(LikedUsersListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final LikedUsersListResp.Snippet elem : resp.getData()) {
                            results.add(new LikedItemViewModel(elem.getUserImage(), elem.getUserName()));
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
