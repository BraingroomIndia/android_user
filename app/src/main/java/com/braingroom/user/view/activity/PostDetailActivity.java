package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.braingroom.user.R;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.viewmodel.ConnectFeedDetailViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class PostDetailActivity extends BaseActivity implements ConnectUiHelper{


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ConnectFeedDetailViewModel(getIntentString("postId"), this, getHelperFactory(), getMessageHelper(), getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void openCommentsFragment(String postId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, CommentFragment.newInstance(postId)).addToBackStack(null).commit();
    }

    @Override
    public void openReplyFragment(String postId, String commentId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, ReplyFragment.newInstance(postId, commentId)).addToBackStack(null).commit();
    }

    @Override
    public void openLikesFragment(String postId, String commentId, String replyId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, LikesFragment.newInstance(postId, commentId, replyId)).addToBackStack(null).commit();
    }

    @Override
    public void openConnectPost() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, ConnectPostFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void popFragment() {
        popBackstack();
    }

}
