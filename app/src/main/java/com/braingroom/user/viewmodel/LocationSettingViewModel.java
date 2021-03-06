package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.HomeActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/*
 * Created by godara on 08/12/17.
 */

public class LocationSettingViewModel extends CustomDialogViewModel {

    public final ListDialogViewModel1 countryVm, cityVm;
    public final Action onResetClicked, onSaveClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Integer>> countryConsumer;
    private final MessageHelper messageHelper;
    private final HashMap<String, Integer> selectedCountries, selectedCity;
    private final Runnable restartActivity;

    public LocationSettingViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, @Nullable Runnable restartActivity) {
        this.navigator = navigator;
        this.messageHelper = messageHelper;
        this.restartActivity = restartActivity;
        selectedCountries = new HashMap<>();
        selectedCity = new HashMap<>();
        if (pref.getString(Constants.SAVED_COUNTRY_NAME, null) != null)
            selectedCountries.put(pref.getString(Constants.SAVED_COUNTRY_NAME, ""), pref.getInt(Constants.SAVED_COUNTRY_ID, 0));

        if (pref.getString(Constants.SAVED_CITY_NAME, null) != null)
            selectedCity.put(pref.getString(Constants.SAVED_CITY_NAME, ""), pref.getInt(Constants.SAVED_CITY_ID, 0));
        countryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    cityVm.reInit(getCityApiObservable(selectedId));
                }
            }
        };

        countryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Country", messageHelper, apiService.getMajorCountry().map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), snippet.getId());
                }
                return new ListDialogData1(itemMap);
            }
        }), selectedCountries, false, countryConsumer, "");


        cityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "City", messageHelper, getCityApiObservable((selectedCountries.values().iterator().hasNext() ? selectedCountries.values().iterator().next() + "" : "-1")), selectedCity, false, null, "select a country first");


        onResetClicked = new Action() {
            @Override
            public void run() throws Exception {
                reset();
            }
        };
        onSaveClicked = new Action() {
            @Override
            public void run() throws Exception {
                dismissDialog();
                apply();
            }
        };

    }

/*    private Observable<ListDialogData1> getStateApiObservable(String countryId) {
        return apiService.getState(countryId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), snippet.getId());
                }
                return new ListDialogData1(itemMap);
            }
        });
    }*/

    private Observable<ListDialogData1> getCityApiObservable(String countryId) {
        if (Integer.parseInt(countryId) == -1)
            return null;
        return apiService.getMajorCity(countryId).observeOn(AndroidSchedulers.mainThread()).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), snippet.getId());
                }
                return new ListDialogData1(itemMap);
            }
        });
    }

    public void reset() {
        countryVm.reset();
        cityVm.reset();
        editor.remove(Constants.SAVED_CITY_ID).apply();
    }

    public void apply() {
        if (countryVm.selectedItemsMap.isEmpty()) {
            messageHelper.show("Please select a country");
        } else if (cityVm.selectedItemsMap.isEmpty()) {
            messageHelper.show("Please select a city");
        } else {
            editor.putString(Constants.SAVED_CITY_NAME, cityVm.selectedItemsMap.keySet().iterator().next()).apply();
            editor.putInt(Constants.SAVED_CITY_ID, cityVm.selectedItemsMap.values().iterator().next()).apply();
            editor.putString(Constants.SAVED_COUNTRY_NAME, countryVm.selectedItemsMap.keySet().iterator().next()).apply();
            editor.putInt(Constants.SAVED_COUNTRY_ID, countryVm.selectedItemsMap.values().iterator().next()).apply();
            Constants.GEO_TAG = "";
            apiService.checkGeoDetail(restartActivity);

        }


    }
}
