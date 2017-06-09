package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.DynamicSearchSelectListFragment;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.view.fragment.Signup1Fragment;
import com.braingroom.user.view.fragment.Signup2Fragment;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.SignupViewModel;

import lombok.Getter;

public class SignupActivity extends BaseActivity {

    public static final String FRAGMENT_TITLE_COUNTRY = "country";
    public static final String FRAGMENT_TITLE_STATE = "state";
    public static final String FRAGMENT_TITLE_CITY = "city";
    public static final String FRAGMENT_TITLE_LOCALITY = "locality";
    public static final String FRAGMENT_UG_COLLEGE = "UG College";
    public static final String FRAGMENT_PG_COLLEGE = "PG Collage";

    public interface UiHelper {

        void next();

        void back();

    }

    @Getter
    SignupViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setElevation(0);
        viewModel = new SignupViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), new UiHelper() {
            @Override
            public void next() {
                changeToSecondFragment();
            }

            @Override
            public void back() {
                popBackstack();
            }
        }, new FragmentHelper() {
            @Override
            public void show(String tag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                transaction.add(R.id.fragment_container, SearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();

            }

            @Override
            public void remove(String tag) {
                popBackstack(tag);
            }
        }, new FragmentHelper() {
            @Override
            public void show(String tag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                transaction.add(R.id.fragment_container, DynamicSearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();

            }

            @Override
            public void remove(String tag) {
                popBackstack(tag);
            }
        });
//        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, Signup1Fragment.newInstance()).addToBackStack(null).commit();

    }

    public void changeToSecondFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, Signup2Fragment.newInstance()).addToBackStack(null).commit();
    }

//    public void changeToFirstFragment() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
//        transaction.replace(R.id.fragment_container, Signup1Fragment.newInstance()).addToBackStack(null).commit();
//    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    @NonNull
    public FragmentHelper getFragmentHelper() {
        return new FragmentHelper() {
            @Override
            public void show(String tag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
                transaction.replace(R.id.fragment_container, ConnectPostFragment.newInstance()).addToBackStack(tag).commit();
            }

            @Override
            public void remove(String tag) {

            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ViewModel getFragmentViewmodel(String title) {
        if (FRAGMENT_TITLE_COUNTRY.equals(title))
            return viewModel.countryVm;
        if (FRAGMENT_TITLE_STATE.equals(title))
            return viewModel.stateVm;
        if (FRAGMENT_TITLE_CITY.equals(title))
            return viewModel.cityVm;
        if (FRAGMENT_TITLE_LOCALITY.equals(title))
            return viewModel.localityVM;
        if (FRAGMENT_UG_COLLEGE.equals(title))
            return viewModel.ugInstituteVm;
        if (FRAGMENT_PG_COLLEGE.equals(title))
            return viewModel.pgInstituteVm;
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().executePendingTransactions();
        int fragCount = getSupportFragmentManager().getBackStackEntryCount();
        if (fragCount == 0) finish();
    }
}
