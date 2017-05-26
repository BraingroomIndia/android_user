package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.model.request.CommentViewReply;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

public class ReplyItemViewModel extends ViewModel {

    public final Action onLikeClicked, onReplyClicked;
    public final String userImage, userName, reply, replyDate;
//    public final ObservableInt numLikes = new ObservableInt();
//    public final ObservableBoolean liked = new ObservableBoolean();

    public ReplyItemViewModel(@NonNull final String postId, @NonNull final String commentId,@NonNull CommentViewReply.Snippet replyData,final ConnectUiHelper uiHelper) {

        userImage = replyData.getUserImage();
        userName = replyData.getUserName();
        reply = replyData.getReply();
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
                uiHelper.openReplyFragment(postId,commentId);

            }
        };

    }

}
