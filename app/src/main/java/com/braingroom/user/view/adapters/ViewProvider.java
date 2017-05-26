
package com.braingroom.user.view.adapters;

import android.support.annotation.LayoutRes;

import com.braingroom.user.viewmodel.ViewModel;


public interface ViewProvider {
    @LayoutRes int getView(ViewModel vm);
}
