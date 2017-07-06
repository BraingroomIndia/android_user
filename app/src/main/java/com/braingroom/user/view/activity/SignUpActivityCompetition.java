package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.request.ProfileUpdateReq;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.DynamicSearchSelectListFragment;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.view.fragment.SignUpCompetition1Fragment;
import com.braingroom.user.view.fragment.SignUpCompetition2Fragment;
import com.braingroom.user.view.fragment.Signup3Fragment;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.SignUpViewModelCompetition;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import lombok.Getter;

/**
 * Created by himan on 6/7/2017.
 */

public class SignUpActivityCompetition extends BaseActivity {

    public static final String FRAGMENT_TITLE_COUNTRY = "country";
    public static final String FRAGMENT_TITLE_STATE = "state";
    public static final String FRAGMENT_TITLE_CITY = "city";
    public static final String FRAGMENT_TITLE_LOCALITY = "locality";
    public static final String FRAGMENT_UG_COLLEGE = "School List ";

    RxPermissions rxPermissions;

    public interface UiHelper {

        void next();

        void back();

        void editMobileNumber(final String uuId);
    }

    @Getter
    SignUpViewModelCompetition viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setElevation(0);
        viewModel = new SignUpViewModelCompetition(getMessageHelper(), getNavigator(), getHelperFactory(), new SignUpActivityCompetition.UiHelper() {
            @Override
            public void next() {
                changeToSecondFragment();
            }

            @Override
            public void back() {
                popBackstack();
            }

            public void editMobileNumber(final String uuid) {
                new MaterialDialog.Builder(SignUpActivityCompetition.this)
                        .title("Contact details")
                        .content("Please enter your mobile number")
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(10, 10)
                        .positiveText("Done")
                        .input("Mobile", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (input.length() > 10 || input.length() < 10) {
                                    getMessageHelper().show("Mobile number must be exactly 10 characters long");
                                } else {
                                    final String mobile = input.toString();
                                    ProfileUpdateReq.Snippet snippet = new ProfileUpdateReq.Snippet();
                                    snippet.setUuid(uuid);
                                    snippet.setMobile(mobile);
                                    getMessageHelper().showProgressDialog("Wait", "Updating Mobile Number");
                                    vm.apiService.updateProfile(new ProfileUpdateReq(snippet)).subscribe(new Consumer<CommonIdResp>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull CommonIdResp commonIdResp) throws Exception {
                                            getMessageHelper().dismissActiveProgress();
                                            try {
                                                viewModel.requestOTP(mobile);
                                            } catch (Exception e) {
                                                Log.d("Cast error", "accept:" + e.toString());
                                            }

                                        }
                                    });

                                }
                            }
                        }).dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                }).show();
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
        transaction.replace(R.id.fragment_container, SignUpCompetition1Fragment.newInstance()).addToBackStack(null).commit();

    }

    public void changeToSecondFragment() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_SMS).subscribe();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.fragment_container, Signup3Fragment.newInstance()).addToBackStack(null).commit();
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

