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

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.view.adapters.ViewModelBinder;
import com.braingroom.user.viewmodel.ViewModel;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.internal.Preconditions;


/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library
 */
public abstract class MvvmActivity extends AppCompatActivity {
    public ViewDataBinding binding;
    protected ViewModel vm;
    protected Bundle extras;
    protected Tracker mTracker;
    protected FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    @Named("defaultPref")
    public SharedPreferences pref;

    @Inject
    @Named("defaultPrefEditor")
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserApplication.getInstance().getMAppComponent().inject(this);
        init(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (getIntent().getExtras() != null)
            extras = getIntent().getExtras().getBundle("classData");
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        vm = createViewModel();
        vm.setMFirebaseAnalytics(mFirebaseAnalytics);
        vm.setMTracker(mTracker);
        getDefaultBinder().bind(binding, vm);
        vm.apiService.checkGeoDetail();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        getDefaultBinder().bind(binding, null);
        binding.executePendingBindings();
        super.onDestroy();
    }

    @NonNull
    protected FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    @NonNull
    protected Tracker getGoogleTracker() {
        return mTracker;
    }

    @NonNull
    protected ViewModelBinder getDefaultBinder() {
        ViewModelBinder defaultBinder = BindingUtils.getDefaultBinder();
        Preconditions.checkNotNull(defaultBinder, "Default Binder");
        return defaultBinder;
    }

    public String getIntentString(String key) {
        if (extras != null) {
            return extras.getString(key);
        } else return null;

    }

    public int getIntentInt(String key) {
        if (extras != null) {
            return extras.getInt(key);
        } else return 0;

    }

    public float getIntentFloat(String key) {
        if (extras != null) {
            return extras.getFloat(key);
        } else return 0;

    }

    public boolean getIntentBoolean(String key) {
        if (extras != null) {
            return extras.getBoolean(key);
        } else return false;

    }

    public Serializable getIntentSerializable(String key) {
        if (extras != null) {
            return extras.getSerializable(key);
        } else return null;

    }

    protected void init(Context ctx) {
        try {

            if (mTracker == null && ctx != null) {
                mTracker = GoogleAnalytics.getInstance(ctx).newTracker(R.xml.global_tracker);
            }
        } catch (Exception e) {
            Log.d("Notification", "init, e=" + e);
        }


    }

    @NonNull
    protected abstract ViewModel createViewModel();

    @LayoutRes
    protected abstract int getLayoutId();
}
