package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.functions.Function;

public class NgoFormViewModel extends ViewModel {

    public final DataItemViewModel recipientsName;
    public final DataItemViewModel emailAddress;
    public final DataItemViewModel mobileNumber;
    public final DataItemViewModel denomination;
    public final DataItemViewModel personalisedMsg;
    public final ListDialogViewModel1 classTopicVm, ngoTypeVm;


    public NgoFormViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                            @NonNull HelperFactory helperFactory, String giftcardId) {
        emailAddress = new DataItemViewModel("");
        denomination = new DataItemViewModel("");
        recipientsName = new DataItemViewModel("");
        mobileNumber = new DataItemViewModel("");
        personalisedMsg = new DataItemViewModel("");
        ngoTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Ngo name", messageHelper, apiService.getNgoList(giftcardId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), false, null,"");

        classTopicVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, apiService.getNgoCategories(giftcardId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), false, null,"");

    }


}
