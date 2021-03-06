package com.braingroom.user.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.request.ProfileUpdateReq;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.OTPViewModel;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.braingroom.user.view.activity.LoginActivity.TAG;

/**
 * Created by godara on 01/07/17.
 */

public class OTPReqFragment extends BaseFragment {

    static SignUpResp.Snippet data;

    public static OTPReqFragment newInstance(SignUpResp.Snippet snippet) {
        data = snippet;
        return new OTPReqFragment();
    }

    public interface UiHelper {
        void editMobileNumber(String uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (data == null)
            activity.popBackstack();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new OTPViewModel(activity.getMessageHelper(), activity.getNavigator(), data, new UiHelper() {
            public void editMobileNumber(final String uuid) {
                new MaterialDialog.Builder(getContext())
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
                                    activity.getMessageHelper().show("Mobile number must be exactly 10 characters long");
                                } else {
                                    final String mobile = input.toString();
                                    ProfileUpdateReq.Snippet snippet = new ProfileUpdateReq.Snippet();
                                    snippet.setUuid(uuid);
                                    snippet.setMobile(mobile);
                                    activity.getMessageHelper().showProgressDialog("Wait", "Updating Mobile Number");
                                    vm.apiService.updateProfile(new ProfileUpdateReq(snippet)).subscribe(new Consumer<CommonIdResp>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull CommonIdResp commonIdResp) throws Exception {
                                            activity.getMessageHelper().dismissActiveProgress();
                                            try {
                                                ((OTPViewModel) vm).requestOTP(mobile);
                                            } catch (Exception e) {
                                                Timber.tag(TAG).w(e, "Cast error");
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
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_otp_req;
    }
}
