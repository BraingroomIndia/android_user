package com.braingroom.user.view.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.UserApplication;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.ViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Data;
import lombok.Getter;


public abstract class BaseActivity extends MvvmActivity {


    @Getter
    ScreenDims screenDims;

    MaterialDialog progressDialog;
    MaterialDialog datePickerDialog;
    public boolean isActive;

    @Inject
    @Named("defaultPref")
    SharedPreferences pref;

    @Inject
    @Named("defaultPrefEditor")
    SharedPreferences.Editor editor;


    @Data
    public static class ScreenDims {
        int height;
        int width;
    }

    private MessageHelper messageHelper;
    private Navigator navigator;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        UserApplication.getInstance().getMAppComponent().inject(this);
    screenDims = new ScreenDims();
        Point size = new Point();
        WindowManager w = getWindowManager();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            screenDims.width = size.x;
            screenDims.height = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            screenDims.width = d.getWidth();
            screenDims.height = d.getHeight();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @NonNull
    public HelperFactory getHelperFactory() {
        return new HelperFactory(this);
    }

    @NonNull
    public Navigator getNavigator() {
        if (navigator == null)
            navigator = new Navigator() {
                @Override
                public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
                    Intent intent = new Intent(BaseActivity.this, destination);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                }

                @Override
                public void navigateActivityForResult(Class<?> destination, @Nullable Bundle bundle, int reqCode) {
                    Intent intent = new Intent(BaseActivity.this, destination);
                    intent.putExtra("data", bundle);
                    startActivityForResult(intent, reqCode);
                }

                @Override
                public void navigateActivity(Intent intent) {
                    startActivity(intent);
                }

                @Override
                public void finishActivity() {
                    finish();
                }

                @Override
                public void finishActivity(Intent resultData) {
                    setResult(RESULT_OK, resultData);
                    finish();
                }

                @Override
                public void openStandaloneYoutube(String videoId) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(BaseActivity.this, "AIzaSyBsaNQgFsk2LbSmXydzNAhBdsQ4YkzAoh0", videoId);
                    startActivity(intent);
                }

                @Override
                public void launchImageChooserActivity(int reqCode) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), reqCode);
                }

                @Override
                public void launchPlaceSearchIntent(int reqCode) {
                    try {
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                        .build(BaseActivity.this);
                        startActivityForResult(intent, reqCode);
                    } catch (GooglePlayServicesRepairableException e) {
                        Log.d("Place Api", "launchPlaceSearchIntent: " + e.getMessage());
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Log.d("Place Api", "launchPlaceSearchIntent: " + e.getMessage());
                    }
                }
            };
        return navigator;
    }

    @NonNull
    public MessageHelper getMessageHelper() {
        if (messageHelper == null)
            messageHelper = new MessageHelper() {
                @Override
                public void show(String message) {
                    dismissActiveProgress();
                    Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void showLoginRequireDialog(String content) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(BaseActivity.this);
                    builder.title("Login required");
                    builder.content(content);
                    builder.positiveText("Okay").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            getNavigator().navigateActivity(LoginActivity.class, null);
                            getNavigator().finishActivity();
                        }
                    }).show();
                }

                @Override
                public void showDismissInfo(String title, String content) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(BaseActivity.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.positiveText("Dismiss").show();
                }

                @Override
                public void showAcceptableInfo(@Nullable String title, @NonNull String content, @NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(BaseActivity.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.onPositive(positiveCallback);
                    builder.positiveText("OK").show();

                }

                @Override
                public void showProgressDialog(@Nullable String title, @NonNull String content) {
                    progressDialog = new MaterialDialog.Builder(BaseActivity.this)
                            .title(title)
                            .content(content)
                            .progress(true, 0)
//                .canceledOnTouchOutside(false)
                            .show();

                }


                @Override
                public void dismissActiveProgress() {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            };
        return messageHelper;
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        vm.handleActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public void popBackstack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void popBackstack(String title) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStack(title, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public ViewModel getFragmentViewmodel(String title) {
        return new ViewModel();
    }

}
