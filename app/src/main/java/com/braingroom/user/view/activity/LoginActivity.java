package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.view.fragment.OTPReqFragment;
import com.braingroom.user.viewmodel.FirsLoginDialogViewModel;
import com.braingroom.user.viewmodel.LoginViewmodel;
import com.braingroom.user.viewmodel.ViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.Serializable;

public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;

    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    LoginButton mFbLogin;

    MaterialDialog forgotPassDialog;
    UIHandler uiHandler;

    ActionBar actionBar;

    String classId;
    String thirdPartyUserId;
    String parentActivity;
    Serializable classData;
    String referralCode;
    String origin;
    String catalogueId;
    public static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getMessageHelper().show("Failed connecting to google");

    }

    public interface UIHandler {
        void showForgotPassDialog();

        void fbLogin();

        void googleLogin();

        void showEmailDialog(LoginResp resp);

        void changeToOTPFragment(SignUpResp.Snippet snippet);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFbLogin = (LoginButton) findViewById(R.id.fb_login_original);
        callbackManager = CallbackManager.Factory.create();
        mFbLogin.setReadPermissions("email");
        Intent intent = getIntent();
        classId = intent.getStringExtra("classId");
        thirdPartyUserId = intent.getStringExtra("thirdPartyUserId");

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        getMessageHelper().showProgressDialog("Logging in", "Sit back while we connect you...");
                        AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                ((LoginViewmodel) vm).setFacebookUser(user);
                                ((LoginViewmodel) vm).socialLogin("facebook");
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,picture");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        getMessageHelper().show("Login cancelled by user");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "onError: " + exception.toString());
                        getMessageHelper().show("Facebook login error");
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        uiHandler = new UIHandler() {
            @Override
            public void showForgotPassDialog() {
                forgotPassDialog = new MaterialDialog.Builder(LoginActivity.this)
                        .title("Password recovery")
                        .content("Please enter your email")
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(4, 30)
                        .positiveText("SUBMIT")
                        .input("Email", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                getMessageHelper().showProgressDialog("Resetting Password", "Sending Email...");
                                ((LoginViewmodel) vm).forgotPassword(input.toString());
                            }
                        }).show();


            }

            @Override
            public void fbLogin() {
                mFbLogin.performClick();
            }

            @Override
            public void googleLogin() {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }

            @Override
            public void showEmailDialog(LoginResp resp) {
                showMandatoryEmailPopup(resp);
            }

            @Override
            public void changeToOTPFragment(SignUpResp.Snippet snippet) {
                ((LoginViewmodel) vm).isOTP.set(true);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.fragment_container, OTPReqFragment.newInstance(snippet)).addToBackStack(null).commit();
            }

        };
        ((LoginViewmodel) vm).setUiHandler(uiHandler);
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        parentActivity = getIntentString("backStackActivity");
        classData = getIntentSerializable("classData");
        classId = getIntentString("id");
        referralCode = getIntentString("referralCode");
        origin = getIntentString("origin");
        catalogueId = getIntentString("catalogueId");

        return new LoginViewmodel(getMessageHelper(), getNavigator(), parentActivity, classData, classId, catalogueId, origin, thirdPartyUserId, referralCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vm.loggedIn.get()) {
            getNavigator().navigateActivity(HomeActivity.class, null);
            finish();
        }
    }


    public void showMandatoryEmailPopup(LoginResp loginResp) {
        FirsLoginDialogViewModel firsLoginDialogViewModel =
                new FirsLoginDialogViewModel(loginResp, getMessageHelper(), getNavigator(), parentActivity, classId, classData);
        firsLoginDialogViewModel.setUiHandler(uiHandler);
        getHelperFactory().createDialogHelper().showCustomView(R.layout.content_first_social_login, firsLoginDialogViewModel
                , false);

        /*new MaterialDialog.Builder(LoginActivity.this)
                .title("Contact details")
                .content("Please enter your mobile number")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(10, 10)
                .positiveText("LOGIN")
                .input("Mobile", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.length() > 10 || input.length() < 10) {
                            getMessageHelper().show("Mobile number must be exactly 10 characters long");
                        } else {
                            ((LoginViewmodel) vm).socialLogin(input.toString());
                        }
                    }
                }).dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LoginManager.getInstance().logOut();
            }
        }).show();*/
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                ((LoginViewmodel) vm).setGoogleAccDetails(acct);
//                showMandatoryEmailPopup();
                ((LoginViewmodel) vm).socialLogin("google");
            } else {
                getMessageHelper().show("Unable to sign into google");
            }
        }
        callbackManager.onActivityResult(requestCode, responseCode, data);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getNavigator().navigateActivity(HomeActivity.class, null);
        return true;
    }

}

