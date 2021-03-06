package com.braingroom.user.viewmodel.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.request.CommentViewReply;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ProfileDisplayActivity;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.viewmodel.ImageUploadViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

public class ReplyItemViewModel extends ViewModel {

    public final Action onLikeClicked, onReplyClicked, showthirdpartyProfile;
    public final String userImage, userName, reply, replyDate, userId;
    public final ImageUploadViewModel imageUploadViewModel;

    public ReplyItemViewModel(@NonNull final String postId, @NonNull final String commentId, @NonNull CommentViewReply.Snippet replyData, @NonNull final Navigator navigator, final ConnectUiHelper uiHelper) {

        userImage = replyData.getUserImage();
        userName = replyData.getUserName();
        userId = replyData.getUserId();
        reply = replyData.getReply();
        imageUploadViewModel = new ImageUploadViewModel(R.drawable.avatar_male, userImage);
//        numLikes.set(Integer.parseInt(replyData.getNumLikes()));
        replyDate = "";//replyData.getReplyDate();
        onLikeClicked = new Action() {
            @Override
            public void run() throws Exception {
//                navigator.launchPlaceSearchIntent(PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        };
        onReplyClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openReplyFragment(postId, commentId);

            }
        };

        showthirdpartyProfile = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundleData = new Bundle();

                if (!BG_ID.equalsIgnoreCase(userId)) {
                    bundleData.putString("userId", userId);
                    navigator.navigateActivity(ThirdPartyViewActivity.class, bundleData);
                } else {
                    navigator.navigateActivity(ProfileDisplayActivity.class, null);
                }

            }
        };

    }

}
