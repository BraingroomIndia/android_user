package com.braingroom.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.view.fragment.CouponFormFragment;
import com.braingroom.user.viewmodel.CouponFormDataViewModel;
import com.braingroom.user.viewmodel.CouponFormViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import io.reactivex.functions.Action;

public class CouponFormActivity extends BaseActivity implements PaymentResultListener {

    ViewPager pager;
    public PagerAdapter pagerAdapter;
    public CouponFormViewModel viewmodel;

    @Override
    public void onPaymentSuccess(String razorpayKey) {
        ((CouponFormViewModel) vm).updatePaymentSuccess(razorpayKey);
    }

    @Override
    public void onPaymentError(int i, String reason) {
        getMessageHelper().show("Payment Failure, Reason : " + reason);
    }

    public interface UiHelper {
        void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param);

        void startRazorpayPayment(JSONObject options);
    }

    public CouponFormDataViewModel getViewmodel(int i) {
        return viewmodel.formDataList.get(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Fill up Form");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager, true);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {

        UiHelper uiHelper = new UiHelper() {
            @Override
            public void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param) {
                PayUmoneySdkInitilizer.startPaymentActivityForResult(CouponFormActivity.this, param);
            }

            @Override
            public void startRazorpayPayment(JSONObject options) {
                final Activity activity = CouponFormActivity.this;
                final Checkout co = new Checkout();
                try {
                    co.open(activity, options);
                } catch (Exception e) {
                    getMessageHelper().show("Error in payment: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        };

        viewmodel = new CouponFormViewModel(getIntentInt("couponVal"), getIntentInt("giftType"), getIntentInt("giftBy"), getMessageHelper(), getNavigator(), getHelperFactory(), new Action() {
            @Override
            public void run() throws Exception {
                pagerAdapter.notifyDataSetChanged();
                pager.setCurrentItem(pagerAdapter.getCount() - 1, true);
            }
        }, uiHelper);
        return viewmodel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupon_form;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return CouponFormFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return viewmodel.formDataList.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public CouponFormFragment getFragmentAt(int position) {
            return (CouponFormFragment) registeredFragments.get(position);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_coupon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            try {
                viewmodel.addNewFormData(0, getIntentInt("giftType"), getIntentInt("giftBy"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (id == R.id.action_apply) {
            try {
                viewmodel.submitClicked.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode ==
                PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String paymentId =
                        data.getStringExtra(SdkConstants.PAYMENT_ID);
                if (paymentId != null)
                    ((CouponFormViewModel) vm).updatePaymentSuccess(paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                getMessageHelper().show("paymemt cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                getMessageHelper().show("paymemt failure");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
//                getMessageHelper().show("paymemt failure");
            }
        }
        if (requestCode == vm.REQ_CODE_LOGIN) {
            vm.handleActivityResult(requestCode, resultCode, data);
        }
    }

}
