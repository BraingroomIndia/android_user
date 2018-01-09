package com.braingroom.user.viewmodel.fragment;

import android.content.Intent;
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
import com.braingroom.user.view.fragment.OTPReqFragment;
import com.braingroom.user.model.request.OTPReq;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;


/*
 * Created by godara on 11/07/17.
 */

public class OTPViewModel extends ViewModel {

    private Disposable otpDisposable;

    public final Action submitOTP, onEditMobile, onResendOTP;

    public final MessageHelper messageHelper;

    public final ObservableField<String> OTP, mobileNumber;
    public final String userId, uuid, emailId, password;

    public OTPViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final SignUpResp.Snippet data, @NonNull final OTPReqFragment.UiHelper uiHelper) {
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
                final SubmitOTPReq.Snippet snippet = new SubmitOTPReq.Snippet();
                snippet.setUserId(userId);
                snippet.setOTP(OTP.get());
                apiService.submitOTP(new SubmitOTPReq(snippet)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp resp) throws Exception {
                        if (resp != null && resp.getResCode() != null && resp.getResCode().equals("1")) //TODO
                            if (data.getLoginType().equals("direct"))
                                apiService.login(emailId, password, pref.getString(Constants.FCM_TOKEN, ""))
                                        .subscribe(new Consumer<LoginResp>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {
                                                if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
                                                    LoginResp.Snippet data = loginResp.getData().get(0);
                                                    //login(data.getName(), data.getEmailId(), data.getProfilePic(), data.getId(), data.getUuid());
                                                    editor.putBoolean(Constants.LOGGED_IN, true);
                                                    editor.putString(Constants.NAME, data.getName());
                                                    editor.putString(Constants.EMAIL, data.getEmailId());
                                                    editor.putString(Constants.PROFILE_PIC, data.getProfilePic());
                                                    editor.putString(Constants.BG_ID, data.getId());
                                                    editor.putString(Constants.UUID, data.getUuid());
                                                    editor.commit();
                                                    navigator.finishActivity(new Intent());
                                                } else {
                                                    messageHelper.show(loginResp.getResMsg());
                                                }

                                            }
                                        });
                            else {
                                editor.putBoolean(Constants.LOGGED_IN, true).commit();
                                navigator.finishActivity(new Intent());
                            }
                        else {
                            logOut();
                            messageHelper.show(resp.getResMsg());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        logOut();
                        Timber.tag(TAG).d("submit OTP: " + throwable.toString());
                        // throwable.printStackTrace();

                    }
                });
            }
        };
    }

    public void requestOTP(String mobileNumber) {
        OTPReq.Snippet snippet = new OTPReq.Snippet(userId, mobileNumber);
        this.mobileNumber.set(mobileNumber);
        apiService.requestOTP(new OTPReq(snippet)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull BaseResp resp) throws Exception {
                messageHelper.show(resp.getResMsg());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Timber.tag(TAG).d("request Otp: " + throwable.toString());

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
                Timber.tag(TAG).d("Otp received in signup", "accept: " + otp);
                if (!otp.equals("")) {
                    try {
                        OTP.set(otp);
                        if (!OTP.get().equals("")) {
                            submitOTP.run();
                        }
                    } catch (Exception e) {
                        //  e.printStackTrace();
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


