package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.braingroom.user.R;
import com.braingroom.user.databinding.FragmentRepliesBinding;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ReplyViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ReplyFragment extends BaseFragment {

    public interface UiHelper {
        void scrollToEnd();

        void scrollToTop();
    }

    RecyclerView recyclerView;

    public static ReplyFragment newInstance(String postId, String commentId) {
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        bundle.putString("commentId", commentId);
        ReplyFragment fragment = new ReplyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        recyclerView = ((FragmentRepliesBinding) binding).recyclerview;
        String postId = getStringArguments("postId");
        String commentId = getStringArguments("commentId");
        return new ReplyViewModel(postId, commentId, activity.getHelperFactory(), activity.getMessageHelper(), activity.getNavigator(), ((ConnectUiHelper) activity), new CommentFragment.UiHelper() {
            @Override
            public void scrollToEnd() {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }

            @Override
            public void scrollToTop() {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_replies;
    }
}
