package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.viewmodel.FilterViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.HashMap;

import lombok.Getter;

public class FilterActivity extends BaseActivity {

    public static final String FRAGMENT_TITLE_CITY = "city";
    public static final String FRAGMENT_TITLE_LOCALITY = "locality";
    public static final String FRAGMENT_TITLE_VENDORLIST = "vendorList";

    @Getter
    FilterViewModel viewModel;


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        viewModel = new FilterViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), new FragmentHelper() {
            @Override
            public void show(String tag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, SearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();
            }

            @Override
            public void remove(String tag) {
                popBackstack(tag);
            }
        }, (FilterData) getIntentSerializable(Constants.classFilterData), (HashMap<String, Integer>) getIntentSerializable(Constants.categoryFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.segmentsFilterMap)
                , (HashMap<String, String>) getIntentSerializable(Constants.cityFilterMap)
                , (HashMap<String, String>) getIntentSerializable(Constants.localityFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.communityFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.classTypeFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.classScheduleFilterMap)
                , (HashMap<String, String>) getIntentSerializable(Constants.vendorListFilterMap)
                , /*getIntentString("keywords")
                , getIntentString("startDate")
                , getIntentString("endDate"),*/getIntentString(Constants.origin));
        return viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all) {
            ((FilterViewModel) vm).reset();
        }
        if (item.getItemId() == R.id.action_apply) {
            ((FilterViewModel) vm).apply();
        }
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public ViewModel getFragmentViewmodel(String title) {
        if (FRAGMENT_TITLE_CITY.equals(title))
            return viewModel.cityVm;
        if (FRAGMENT_TITLE_LOCALITY.equals(title))
            return viewModel.localityVm;
        if (FRAGMENT_TITLE_VENDORLIST.equals(title))
            return viewModel.vendorListVm;
        return null;
    }

}
