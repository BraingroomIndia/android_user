package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.braingroom.user.R;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.PostAcceptFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.viewmodel.ConnectFeedDetailViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.List;

public class PostDetailActivity extends BaseActivity implements ConnectUiHelper {


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String notificationId = getIntentString("notification_id");
        if (vm.loggedIn.get() && !TextUtils.isEmpty(notificationId))
            vm.apiService.changeNotificationStatus(notificationId).subscribe();
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
    public void openAcceptedUsersFragment(String postId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, PostAcceptFragment.newInstance(postId)).addToBackStack(null).commit();

    }

    @Override
    public void setFilterData(String keyword, String categoryId, String segmentId, String myGroupId, String allGroupId,
                              String instituteId, String authorId, List<String> location) {
    }

    @Override
    public void popFragment() {
        popBackstack();
    }

    @Override
    public void retry() {
    }

    @Override
    public void setSearchQuery(String a) {
    }

    @Override
    public void setCount(int notificationCount, int messageCount) {
    }

    public void openFilter() {
    }
}
