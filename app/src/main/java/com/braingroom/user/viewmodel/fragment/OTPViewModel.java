package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.request.SubmitOTPReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.fragment.OTPReqFragment;
import com.braingroom.user.viewmodel.OTPReq;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.rollbar.android.Rollbar.TAG;

/**
 * Created by godara on 11/07/17.
 */

public class OTPViewModel extends ViewModel {

    private Disposable otpDisposable;

    public final Action submitOTP, onEditMobile, onResendOTP;

    public final MessageHelper messageHelper;

    public final ObservableField<String> OTP, mobileNumber;
    public final String userId, uuid, emailId, password;

    public OTPViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull SignUpResp.Snippet data, @NonNull final OTPReqFragment.UiHelper uiHelper) {
        this.OTP = new ObservableField<>("");
        this.mobileNumber = new ObservableField<>(data.getMobileNumber());
        this.messageHelper = messageHelper;
        userId = data.getUserId();
        uuid = data.getUuid();
        emailId = data.getEmailId();
        password = data.getPassword();
        requestOTP(data.getMobileNumber());


        onEditMobile = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.editMobileNumber(uuid);
            }
        };
        onResendOTP = new Action() {
            @Override
            public void run() throws Exception {
                requestOTP(mobileNumber.get());
            }
        };


        submitOTP = new Action() {
            @Override
            public void run() throws Exception {
                if (OTP.get().equals("")) {
                    messageHelper.show("Please enter OTP");
                    return;
                }
                SubmitOTPReq.Snippet snippet = new SubmitOTPReq.Snippet();
                snippet.setUserId(userId);
                snippet.setOTP(OTP.get());
                apiService.submitOTP(new SubmitOTPReq(snippet)).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp resp) throws Exception {
                        if (resp.getResCode().equals("1"))
                            apiService.login(emailId, password, pref.getString(Constants.FCM_TOKEN, ""))
                                    .subscribe(new Consumer<LoginResp>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {
                                            if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
                                                editor.putBoolean(Constants.LOGGED_IN, true);
                                                editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                                                editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                                                editor.commit();
                                                navigator.navigateActivity(HomeActivity.class, null);
                                            }

                                        }
                                    });
                        else messageHelper.show(resp.getResMsg());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Log.d(TAG, "submit OTP: " + throwable.toString());
                        throwable.printStackTrace();

                    }
                });
            }
        };
    }

    public void requestOTP(String mobileNumber) {
        OTPReq.Snippet snippet = new OTPReq.Snippet(userId, mobileNumber);
        this.mobileNumber.set(mobileNumber);
        apiService.requestOTP(new OTPReq(snippet)).subscribe(new Consumer<BaseResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull BaseResp resp) throws Exception {
                messageHelper.show(resp.getResMsg());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d(TAG, "request Otp: " + throwable.toString());

            }
        });
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }


    @Override
    public void onResume() {
        otpDisposable = UserApplication.getInstance().getOtpArrived().subscribe(new Consumer<String>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull String otp) throws Exception {
                Log.d("Otp received in signup", "accept: " + otp);
                if (!otp.equals("")) {
                    try {
                        OTP.set(otp);
                        if (!OTP.get().equals("")) {
                            submitOTP.run();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        safelyDispose(otpDisposable);
    }
}

