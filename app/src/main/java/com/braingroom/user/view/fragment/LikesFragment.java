package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.LikesViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class LikesFragment extends BaseFragment {

    public static LikesFragment newInstance(String postId, String commentId, String replyId) {
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        bundle.putString("commentId", commentId);
        bundle.putString("replyId", replyId);
        LikesFragment fragment = new LikesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String postId = getStringArguments("postId");
        String commentId = getStringArguments("commentId");
        String replyId = getStringArguments("replyId");
        return new LikesViewModel(postId, commentId, replyId, ((ConnectUiHelper) activity), activity.getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_likes;
    }
}
