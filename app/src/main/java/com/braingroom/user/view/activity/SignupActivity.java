package com.braingroom.user.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.utils.SmsReceiver;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.DynamicSearchSelectListFragment;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.view.fragment.Signup1Fragment;
import com.braingroom.user.view.fragment.Signup2Fragment;
import com.braingroom.user.view.fragment.OTPReqFragment;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.SignupViewModel;
import com.braingroom.user.viewmodel.fragment.DynamicSearchSelectListViewModel;
import com.tbruyelle.rxpermissions2.RxPermissions;
/*import com.zoho.livechat.android.MbedableComponent;
import com.zoho.salesiqembed.ZohoSalesIQ;*/

import io.reactivex.functions.Consumer;
import lombok.Getter;

public class SignupActivity extends BaseActivity {

    public static final String FRAGMENT_TITLE_COUNTRY = "country";
    public static final String FRAGMENT_TITLE_STATE = "state";
    public static final String FRAGMENT_TITLE_CITY = "city";
    public static final String FRAGMENT_TITLE_LOCALITY = "locality";
    public static final String FRAGMENT_UG_COLLEGE = "UG College";
    public static final String FRAGMENT_PG_COLLEGE = "PG Collage";

    RxPermissions rxPermissions;


    public interface UiHelper {

        void firstFragment();

        void secondFragment();

        void thirdFragment(SignUpResp.Snippet snippet);

        void back();


    }

    private String referralCode;

    @Getter
    SignupViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // ZohoSalesIQ.init(getApplication(), "vbaQbJT6pgp%2F3Bcyb2J5%2FIhGMQOrLMwCtSBDWvN719iFMGR6B8HQyg%2BYib4OymZbE8IA0L0udBo%3D", "689wH7lT2QpWpcVrcMcCOyr5GFEXO50qvrL9kW6ZUoJBV99ST2d97x9bQ72vOdCZvEyaq1slqV%2BhFd9wYVqD4%2FOv9G5EQVmggE5fHIGwHTu%2BOv301MhrYfOQ0d2CzZkt0qlz0ytPLErfXRYn5bu%2FGGbVJmRXRnWU");
//        getSupportActionBar().setElevation(0);
/*        try {
          //  ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
        } catch (Exception e){e.printStackTrace();}*/
        SmsReceiver smsReceiver = new SmsReceiver();
        viewModel = (SignupViewModel) vm;

//        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, Signup1Fragment.newInstance()).addToBackStack(null).commit();

        //Sms Read permission
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.RECEIVE_SMS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                Log.d("Message permission", "accept: " + aBoolean.toString());
            }
        });

    }


    public void changeToFirstFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, Signup1Fragment.newInstance()).addToBackStack(null).commit();
    }

    public void changeToSecondFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, Signup2Fragment.newInstance()).addToBackStack(null).commit();
    }

    public void changeToOTPFragment(SignUpResp.Snippet snippet) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, OTPReqFragment.newInstance(snippet)).addToBackStack(null).commit();
    }

//    public void changeToFirstFragment() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
//        transaction.replace(R.id.fragment_container, Signup1Fragment.newInstance()).addToBackStack(null).commit();
//    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        referralCode = getIntentString("referralCode");
        return new SignupViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), new UiHelper() {

            @Override
            public void firstFragment() {
                changeToFirstFragment();
            }

            @Override
            public void secondFragment() {
                changeToSecondFragment();
            }

            @Override
            public void thirdFragment(SignUpResp.Snippet snippet) {
                changeToOTPFragment(snippet);
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
        }, referralCode);
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
        if (DynamicSearchSelectListViewModel.FRAGMENT_TITLE_COLLEGE.equals(title))
            return viewModel.ugInstituteVm;
        if (DynamicSearchSelectListViewModel.FRAGMENT_TITLE_COLLEGE.equals(title))
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
