package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ReviewAddViewModel;

import java.io.Serializable;

import io.reactivex.functions.Action;

/**
 * Created by ashketchup on 8/12/17.
 */

public class ReviewFragment extends BaseFragment {
    public static ReviewFragment newInstance(int reviewType, String id, ReviewAddViewModel.ReviewAddHelper  optional) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("reviewType",reviewType);
        bundle.putSerializable("key",(Serializable)optional);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    protected ViewModel createViewModel() {
        int reviewType=getArguments().getInt("reviewType");
        String id=getStringArguments("id");
        ReviewAddViewModel.ReviewAddHelper reviewAddHelper = (ReviewAddViewModel.ReviewAddHelper)getSerializableArguments("key");
        return new ReviewAddViewModel(id,reviewType,reviewAddHelper);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_review;
    }
}
