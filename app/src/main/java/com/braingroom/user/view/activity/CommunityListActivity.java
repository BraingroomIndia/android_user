package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.CommunityListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 09/08/17.
 */

public class CommunityListActivity extends BaseActivity {
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new CommunityListViewModel(getMessageHelper(),getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community_list;
    }
}
