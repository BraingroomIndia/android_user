package com.braingroom.user.view.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.viewmodel.ThirdPartyViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import static com.braingroom.user.view.activity.ConnectHomeActivity.BUY_SELL;
import static com.braingroom.user.view.activity.ConnectHomeActivity.DISCUSS_DECIDE;
import static com.braingroom.user.view.activity.ConnectHomeActivity.FIND_PARTNERS;
import static com.braingroom.user.view.activity.ConnectHomeActivity.LEARNER_FORUM;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TIPS_TRICKS;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TUTORS_ARTICLE;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TUTORS_TALK;

/**
 * Created by agrahari on 07/04/17.
 */

public class ThirdPartyInfoFragment extends BaseFragment {

    public static ThirdPartyInfoFragment newInstance() {
        return new ThirdPartyInfoFragment();
    }


    public BottomNavigationView mBottomNav;


    int[][] states = new int[][]{
            new int[]{android.R.attr.state_checked}, // enabled
            new int[]{-android.R.attr.state_checked}
    };
    ColorStateList greenList, blueList;
    @IdRes
    int selectedBottomNav = R.id.action_tips_tricks;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().requestFocus();
        greenList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(activity, R.color.bottomNavSelectedGreen),
                ContextCompat.getColor(activity, R.color.bottomNavUnSelected)
        });
        blueList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(activity, R.color.bottomNavSelectedBlue),
                ContextCompat.getColor(activity, R.color.bottomNavUnSelected)
        });
        mBottomNav = getView().findViewById(R.id.bottom_navigation);
        mBottomNav.inflateMenu(R.menu.bottom_nav_profile);
        if (TIPS_TRICKS.equals(((ThirdPartyViewModel) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_tips_tricks;
        if (BUY_SELL.equals(((ThirdPartyViewModel) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_buy_sell;
        if (FIND_PARTNERS.equals(((ThirdPartyViewModel) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_find_partners;
        if (DISCUSS_DECIDE.equals(((ThirdPartyViewModel) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_discuss_n_decide;
        mBottomNav.setSelectedItemId(selectedBottomNav);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                updateBottomNavigation(itemId);
                return true;
            }
        });
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((ThirdPartyViewActivity) activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_thirdparty_profile_1;
    }

    private void updateBottomNavigation(int itemId) {

        ConnectFilterData connectFilterData = new ConnectFilterData();
        connectFilterData.setAuthorId(((ThirdPartyViewModel) vm).userId);
        if (itemId == R.id.action_tips_tricks) {

            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(TIPS_TRICKS);
            ((ThirdPartyViewModel) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_buy_sell) {
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(BUY_SELL);
            ((ThirdPartyViewModel) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_find_partners) {
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(FIND_PARTNERS);
            ((ThirdPartyViewModel) vm).setFilterData(connectFilterData);
        }

        if (itemId == R.id.action_discuss_n_decide) {
            connectFilterData.setMajorCateg(TUTORS_TALK);
            connectFilterData.setMinorCateg(DISCUSS_DECIDE);
            ((ThirdPartyViewModel) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_tutors_article) {

            connectFilterData.setMajorCateg(TUTORS_TALK);
            connectFilterData.setMinorCateg(TUTORS_ARTICLE);
            ((ThirdPartyViewModel) vm).setFilterData(connectFilterData);
        }
    }
}
