package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by godara on 08/12/17.
 */

public class LocationSettingViewModel extends CustomDialogViewModel {

    public final ListDialogViewModel1 countryVm, cityVm;
    public final Action onResetClicked, onSaveClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Integer>> countryConsumer;
    private final MessageHelper messageHelper;

    public LocationSettingViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory) {
        this.navigator = navigator;
        this.messageHelper = messageHelper;
       /* countryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    stateVm.reInit(getStateApiObservable(selectedId));
                }
            }
        };*/
       /* if (pref.getString("connect_country_name", null) != null)
            selectedCountries.put(pref.getString("connect_country_name", ""), pref.getInt("connect_country_id", 0));
        if (pref.getString("connect_state_name", null) != null)
            selectedStates.put(pref.getString("connect_state_name", ""), pref.getInt("connect_state_id", 0));
        if (pref.getString("connect_city_name", null) != null)
            selectedCity.put(pref.getString("connect_city_name", ""), pref.getInt("connect_city_id", 0));
*/
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
        }), new HashMap<String, Integer>(), false, countryConsumer, "");


        cityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "City", messageHelper, getCityApiObservable("-1"), new HashMap<String, Integer>(), false, null, "select a country first");


        onResetClicked = new Action() {
            @Override
            public void run() throws Exception {
                reset();
            }
        };
        onSaveClicked = new Action() {
            @Override
            public void run() throws Exception {
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
        editor.remove(Constants.SAVED_CITY).apply();
    }

    public void apply() {
        if (countryVm.selectedItemsMap.isEmpty()) {
            messageHelper.show("Please select a country");
        } else if (cityVm.selectedItemsMap.isEmpty()) {
            messageHelper.show("Please select a city");
        } else {
            editor.putInt(Constants.SAVED_CITY, cityVm.selectedItemsMap.values().iterator().next()).apply();
            Constants.GEO_TAG = "";
            apiService.checkGeoDetail();
            dismissDialog();
        }


    }
}
