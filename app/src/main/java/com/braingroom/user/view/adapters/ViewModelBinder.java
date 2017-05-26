

package com.braingroom.user.view.adapters;

import android.databinding.ViewDataBinding;

import com.braingroom.user.viewmodel.ViewModel;


public interface ViewModelBinder {
    void bind(ViewDataBinding viewDataBinding, ViewModel viewModel);
}
