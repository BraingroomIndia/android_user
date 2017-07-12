package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.functions.Consumer;
import lombok.Data;
import lombok.Getter;

import static com.braingroom.user.R.id.view;


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

    public Toast toast;

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
        vm.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        vm.onPause();
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
                    intent.putExtra("classData", bundle);
                    startActivity(intent);
                }

                @Override
                public void navigateActivityForResult(Class<?> destination, @Nullable Bundle bundle, int reqCode) {
                    Intent intent = new Intent(BaseActivity.this, destination);
                    intent.putExtra("classData", bundle);
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
                public void openStandaloneVideo(String videoId) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoId));
                    intent.setDataAndType(Uri.parse(videoId), "video/*");
                    startActivity(intent);
                }


                @Override
                public void showMenuPopup(@MenuRes int layout, View v, android.support.v7.widget.PopupMenu.OnMenuItemClickListener clickListner) {
                    PopupMenu popup = new PopupMenu(BaseActivity.this, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(layout, popup.getMenu());
                    popup.setOnMenuItemClickListener(clickListner);
                    popup.show();
                }


                @Override
                public void launchImageChooserActivity(final int reqCode) {

                    RxPermissions rxPermissions = new RxPermissions(BaseActivity.this);
                    rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
                            if (granted) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), reqCode);
                            }
                        }
                    });
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), reqCode);
                }

                @Override
                public void launchVideoChooserActivity(final int reqCode) {

                    RxPermissions rxPermissions = new RxPermissions(BaseActivity.this);
                    rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
                            if (granted) {
                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Video"), reqCode);
                            }
                        }
                    });
//                    Intent intent = new Intent();
//                    intent.setType("video/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), reqCode);
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

                @Override
                public void hideKeyBoard(View view){
                    if (view!=null)
                    {InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);}

                }
            }

                    ;
        return navigator;
    }

    @NonNull
    public MessageHelper getMessageHelper() {
        if (messageHelper == null)
            messageHelper = new MessageHelper() {
                @Override
                public void show(String message) {
                    if (toast != null) {
                        toast.cancel();
                    }
                    dismissActiveProgress();
                    toast = Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void showLoginRequireDialog(String content, final Bundle data) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(BaseActivity.this);
                    builder.title("Login required");
                    builder.content(content);
                    builder.positiveText("Okay").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            getNavigator().navigateActivity(LoginActivity.class, data);
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
