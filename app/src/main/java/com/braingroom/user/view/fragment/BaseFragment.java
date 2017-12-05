package com.braingroom.user.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.view.adapters.ViewModelBinder;
import com.braingroom.user.viewmodel.ViewModel;

import java.io.Serializable;

import dagger.internal.Preconditions;
import lombok.Getter;

/*
 * Created by agrahari on 07/04/17.
*/

public abstract class BaseFragment extends Fragment {

    public ViewDataBinding binding;
    @Getter
    protected ViewModel vm;
    public BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            activity = (BaseActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        vm = createViewModel();
        if (vm == null)
            activity.popBackstack();
        getDefaultBinder().bind(binding, vm);
        return binding.getRoot();
    }

    @Nullable
    public String getStringArguments(String key) {
        return getArguments().getString(key);
    }

    @Nullable
    public Serializable getSerializableArguments(String key) {
        return getArguments().getSerializable(key);
    }

    @Override
    public void onDestroy() {
        getDefaultBinder().bind(binding, null);
        binding.executePendingBindings();
        super.onDestroy();
    }

    @NonNull
    private ViewModelBinder getDefaultBinder() {
        ViewModelBinder defaultBinder = BindingUtils.getDefaultBinder();
        Preconditions.checkNotNull(defaultBinder, "Default Binder");
        return defaultBinder;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    protected abstract ViewModel createViewModel();

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        vm.setLoggedIn();
        vm.onResume();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
