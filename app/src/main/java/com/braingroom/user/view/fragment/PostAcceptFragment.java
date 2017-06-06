package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.PostAcceptViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class PostAcceptFragment extends BaseFragment {

    public static PostAcceptFragment newInstance(String postId, String commentId, String replyId) {
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        PostAcceptFragment fragment = new PostAcceptFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String postId = getStringArguments("postId");
        return new PostAcceptViewModel(postId, ((ConnectUiHelper) activity));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_likes;
    }
}
