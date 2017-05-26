package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.model.response.CommentListResp;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;

public class CommentsItemViewModel extends ViewModel {

    public final Action onReplyClicked;
    public final String commenterImage, commenterName, comment, commentDate;
    public final int numReplies;
    //    public final ObservableInt numLikes = new ObservableInt();
//    public final ObservableBoolean liked = new ObservableBoolean();
    public final String firstReplyImage, firstReplyName, firstReply;

    public CommentsItemViewModel(@NonNull final String postId, @NonNull final CommentListResp.Snippet item, final ConnectUiHelper uiHelper) {

        commenterImage = item.getUserImage();
        commenterName = item.getUserName();
        comment = item.getComment();
        numReplies = item.getReplies() == null ? 0 : item.getReplies().size();
//        numLikes.set(Integer.parseInt(item.getNumLikes()));
        commentDate = item.getDate() == null ? "" : getHumanDate(Long.parseLong(item.getDate()));

        if (numReplies > 0) {
            firstReplyImage = item.getReplies().get(0).getUserImage();
            firstReplyName = item.getReplies().get(0).getUserName();
            firstReply = item.getReplies().get(0).getReply();
        } else {
            firstReplyImage = null;
            firstReplyName = null;
            firstReply = null;
        }
//        onLikeClicked = new Action() {
//            @Override
//            public void run() throws Exception {
////                navigator.launchPlaceSearchIntent(PLACE_AUTOCOMPLETE_REQUEST_CODE);
//            }
//        };
        onReplyClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openReplyFragment(postId,item.getId());
            }
        };

    }

    private String getHumanDate(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
}
