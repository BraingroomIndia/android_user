package com.braingroom.user.view.fragment;

import android.support.annotation.Nullable;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ReviewAddViewModel;

/**
 * Created by ashketchup on 8/12/17.
 */

public class ReviewFragment extends BaseFragment {

    @Nullable
    @Override
    protected ViewModel createViewModel() {
        int reviewType=getArguments().getInt("reviewType");
        String id=getStringArguments("id");
        return new ReviewAddViewModel(id,reviewType);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_review;
    }
}
