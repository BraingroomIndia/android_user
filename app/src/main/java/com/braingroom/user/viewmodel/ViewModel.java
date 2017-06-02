package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.DataflowService;
import com.braingroom.user.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class ViewModel {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;
    public static final int REQ_CODE_CHOOSE_IMAGE = 1;
    public static final int REQ_CODE_CHOOSE_FILTER = 2;
    public static final int REQ_CODE_CHOOSE_VIDEO = 3;
    public static final int REQ_CODE_LOGIN=4;

    @Inject
    public DataflowService apiService;

    @Inject
    Gson gson;

    @Inject
    @Named("defaultPref")

    public SharedPreferences pref;

    @Inject
    @Named("defaultPrefEditor")
    protected
    SharedPreferences.Editor editor;

    public final ObservableField<Boolean> loggedIn = new ObservableField<>(false);

    /*for non reactive adapter recycler views*/
    public
    @NonNull
    List<ViewModel> nonReactiveItems = new ArrayList<>();


    public ViewModel() {
        UserApplication.getInstance().getMAppComponent().inject(this);
        loggedIn.set(pref.getBoolean(Constants.LOGGED_IN, false));
    }

    public void logOut() {
        editor.clear().commit();
        loggedIn.set(false);

    }

    public void init() {
    }

    public void paginate() {
    }


    public void handleActivityResult(final int requestCode, int resultCode, Intent data) {
    }
}
