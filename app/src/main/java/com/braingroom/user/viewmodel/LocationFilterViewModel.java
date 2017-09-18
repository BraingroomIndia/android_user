package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LocationFilterViewModel extends CustomDialogViewModel {

    public final ListDialogViewModel1 countryVm, stateVm, cityVm;
    public final Action onResetClicked, onSaveClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Integer>> countryConsumer, stateConsumer;
    HashMap<String, Integer> selectedCountries = new HashMap<>();
    HashMap<String, Integer> selectedStates = new HashMap<>();
    HashMap<String, Integer> selectedCity = new HashMap<>();

    public LocationFilterViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, final ConnectHomeActivity.UiHelper uiHelper) {
        this.navigator = navigator;
        countryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    stateVm.reInit(getStateApiObservable(selectedId));
                }
            }
        };
        if (pref.getString("connect_country_name", null) != null)
            selectedCountries.put(pref.getString("connect_country_name", ""), pref.getInt("connect_country_id", 0));
        if (pref.getString("connect_state_name", null) != null)
            selectedStates.put(pref.getString("connect_state_name", ""), pref.getInt("connect_state_id", 0));
        if (pref.getString("connect_city_name", null) != null)
            selectedCity.put(pref.getString("connect_city_name", ""), pref.getInt("connect_city_id", 0));

        countryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Country", messageHelper, apiService.getCountry().map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), selectedCountries, false, countryConsumer, "");

        stateConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    cityVm.reInit(getCityApiObservable(selectedId));
                }
            }
        };

        stateVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "State", messageHelper, getStateApiObservable("-1"), selectedStates, false, stateConsumer, "select a country first");
        cityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "City", messageHelper, getCityApiObservable("-1"), selectedCity, false, null, "select a state first");


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
                uiHelper.updateLocationFilter();
            }
        };

    }

    private Observable<ListDialogData1> getStateApiObservable(String countryId) {
        return apiService.getState(countryId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    private Observable<ListDialogData1> getCityApiObservable(String cityId) {
        return apiService.getCityList(cityId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    public void reset() {
        countryVm.reset();
        stateVm.reset();
        cityVm.reset();
        editor.remove("connect_country_id");
        editor.remove("connect_country_name");
        editor.remove("connect_state_id");
        editor.remove("connect_state_name");
        editor.remove("connect_city_id");
        editor.remove("connect_city_name");
        editor.commit();
    }

    public void apply() {
        if (!countryVm.selectedItemsMap.isEmpty()) {
            editor.putInt("connect_country_id", countryVm.selectedItemsMap.values().iterator().next());
            editor.putString("connect_country_name", countryVm.selectedItemsMap.keySet().iterator().next());
        }
        if (!stateVm.selectedItemsMap.isEmpty()) {
            editor.putInt("connect_state_id", stateVm.selectedItemsMap.values().iterator().next());
            editor.putString("connect_state_name", stateVm.selectedItemsMap.keySet().iterator().next());

        }
        if (!cityVm.selectedItemsMap.isEmpty()) {
            editor.putInt("connect_city_id", cityVm.selectedItemsMap.values().iterator().next());
            editor.putString("connect_city_name", cityVm.selectedItemsMap.keySet().iterator().next());
        }
        editor.commit();
        dismissDialog();
    }
}
