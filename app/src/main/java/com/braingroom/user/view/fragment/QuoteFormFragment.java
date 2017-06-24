package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.QuoteFormViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class QuoteFormFragment extends BaseFragment {

    public static QuoteFormFragment newInstance() {
        return new QuoteFormFragment();
    }

    public interface UiHelper{
        void popFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().requestFocus();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new QuoteFormViewModel(activity.getMessageHelper() ,new UiHelper(){
            @Override
            public void popFragment(){
                getActivity().onBackPressed();

            }
        },activity.getHelperFactory());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quote_form;
    }
}
