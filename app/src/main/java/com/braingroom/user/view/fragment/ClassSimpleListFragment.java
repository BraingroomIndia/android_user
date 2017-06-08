package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassSimpleListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ClassSimpleListFragment extends BaseFragment {
    private static String userId;

    public static ClassSimpleListFragment newInstance(String id) {

        Bundle bundle = new Bundle();
        userId = id;
//        bundle.putString("majorCateg", majorCateg);
        ClassSimpleListFragment fragment = new ClassSimpleListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ClassSimpleListViewModel(activity.getMessageHelper(), activity.getNavigator(), "bookinghistory", userId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.class_simplelist;
    }


}
