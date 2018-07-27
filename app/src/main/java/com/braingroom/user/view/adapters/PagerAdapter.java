package com.braingroom.user.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.braingroom.user.view.fragment.CategoriesFragment;
import com.braingroom.user.view.fragment.GroupClassesFragment;
import com.braingroom.user.view.fragment.RecommendedClassesFragment;
import com.braingroom.user.view.fragment.TopSellingClassesFragment;

/**
 * Created by android on 24/07/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CategoriesFragment tab1 = new CategoriesFragment();
                return tab1;
            case 1:
                RecommendedClassesFragment tab2 = new RecommendedClassesFragment();
                return tab2;
            case 2:
                 TopSellingClassesFragment tab3 = new TopSellingClassesFragment();
                return tab3;
            case 3:
                GroupClassesFragment tab4 = new GroupClassesFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
