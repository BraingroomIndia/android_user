package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class SearchSelectListFragment extends BaseFragment {

    public static SearchSelectListFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        SearchSelectListFragment fragment = new SearchSelectListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String title = getStringArguments("title");
        return activity.getFragmentViewmodel(title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_select_list;
    }
}
