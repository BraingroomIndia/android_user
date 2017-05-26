package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.braingroom.user.R;
import com.braingroom.user.databinding.FragmentCommentsBinding;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.CommentsViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class CommentFragment extends BaseFragment {

    public interface UiHelper {
        void scrollToEnd();

        void scrollToTop();
    }

    RecyclerView recyclerView;

    public static CommentFragment newInstance(String postId) {
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        recyclerView = ((FragmentCommentsBinding) binding).recyclerview;
        String postId = getStringArguments("postId");
        return new CommentsViewModel(postId, activity.getHelperFactory(), activity.getMessageHelper(), activity.getNavigator(), ((ConnectUiHelper) activity), new UiHelper() {
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
        return R.layout.fragment_comments;
    }
}
