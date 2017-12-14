package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ReviewAddViewModel;
import com.braingroom.user.viewmodel.fragment.VendorReviewsViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class VendorReviewFragment extends BaseFragment {

    public static VendorReviewFragment newInstance() {
        return new VendorReviewFragment();
    }


    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) floatingActionButton = view.findViewById(R.id.fab);
        return view;
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new VendorReviewsViewModel(activity.getMessageHelper(), new VendorReviewsViewModel.UiHelper() {
            @Override
            public void addReviewFragment() {
                if (floatingActionButton != null)
                    floatingActionButton.setVisibility(View.INVISIBLE);

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, ReviewFragment.newInstance(Constants.vendorReview, activity.getIntentString("id"), new ReviewAddViewModel.ReviewAddHelper() {
                    @Override
                    public void run() {
                        getChildFragmentManager().popBackStack();
                        if (floatingActionButton != null)
                            floatingActionButton.setVisibility(View.VISIBLE);
                    }
                })).addToBackStack(null).commit();
            }
        }, activity.getIntentString("id"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vendor_review_list;
    }
}
