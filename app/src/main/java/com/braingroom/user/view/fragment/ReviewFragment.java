package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ReviewAddViewModel;

import java.io.Serializable;

import io.reactivex.functions.Action;

/**
 * Created by ashketchup on 8/12/17.
 */

public class ReviewFragment extends BaseFragment {
    public static ReviewFragment newInstance(int reviewType, String id, final ReviewAddViewModel.ReviewAddHelper reviewAddHelper) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("reviewType", reviewType);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(bundle);
        fragment.reviewAddHelper = reviewAddHelper;
        return fragment;
    }

    public static ReviewFragment newInstance(int reviewType, String id, String userId, final ReviewAddViewModel.ReviewAddHelper reviewAddHelper) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("reviewType", reviewType);
        bundle.putString(Constants.BG_ID, userId);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(bundle);
        fragment.reviewAddHelper = reviewAddHelper;
        return fragment;
    }

    public ReviewAddViewModel.ReviewAddHelper reviewAddHelper;

    @Nullable
    @Override
    protected ViewModel createViewModel() {
        int reviewType = getArguments().getInt("reviewType");
        String id = getStringArguments("id");
        String userId = getStringArguments(Constants.BG_ID);
        return new ReviewAddViewModel(activity.getMessageHelper(), id, reviewType, reviewAddHelper);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_review;
    }
}
