/*
 * Copyright 2016 Manas Chaudhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.braingroom.user.view.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.view.adapters.ViewModelBinder;
import com.braingroom.user.viewmodel.ViewModel;

import java.io.Serializable;

import dagger.internal.Preconditions;


/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library
 */
public abstract class MvvmActivity extends AppCompatActivity {
    public ViewDataBinding binding;
    protected ViewModel vm;
    protected Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null)
            extras = getIntent().getExtras().getBundle("classData");
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        vm = createViewModel();
        getDefaultBinder().bind(binding, vm);
    }

    @Override
    protected void onDestroy() {
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

    public String getIntentString(String key) {
        if (extras != null) {
            return extras.getString(key);
        } else return null;

    }

    public Serializable getIntentSerializable(String key) {
        if (extras != null) {
            return extras.getSerializable(key);
        } else return null;

    }

    @NonNull
    protected abstract ViewModel createViewModel();

    @LayoutRes
    protected abstract int getLayoutId();
}
