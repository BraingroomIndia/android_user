package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.request.QuoteReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.fragment.QuoteFormFragment;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class QuoteFormViewModel extends ViewModel {

    public final DataItemViewModel organizationName;
    public final DataItemViewModel audienceStrength;
    public final DataItemViewModel hostClassLocation;
    public final DataItemViewModel mobileNumber;
    public final DataItemViewModel classNeedDetail;
    public final DataItemViewModel description;
    public final ListDialogViewModel1 classModeVm;
    public final DatePickerViewModel dateVm;
    public final Action onSubmit;

    public final String mandatory = " <font color=\"#ff0000\">" + "* " + "</font>";


    public QuoteFormViewModel(@NonNull final MessageHelper messageHelper, @NonNull final QuoteFormFragment.UiHelper uiHelper,
                              @NonNull HelperFactory helperFactory, @NonNull final String catalogueId, final String classId) {
        organizationName = new DataItemViewModel("");
        audienceStrength = new DataItemViewModel("");
        hostClassLocation = new DataItemViewModel("");
        mobileNumber = new DataItemViewModel("");
        classNeedDetail = new DataItemViewModel("");
        description = new DataItemViewModel("");
        final LinkedHashMap<String, Integer> classModeData = new LinkedHashMap<>();
        classModeData.put("Workshops", 1);
        classModeData.put("Online classes", 2);
        classModeData.put("Webinars", 3);
        classModeData.put("Classes", 4);
        classModeData.put("Learning events & activities", 5);
        classModeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class mode", messageHelper,
                Observable.just(new ListDialogData1(classModeData)), new HashMap<String, Integer>(), false, null,"");
        dateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "Date", "choose");
        onSubmit =new Action() {
            @Override
            public void run() throws Exception {
                if (organizationName.s_1.get().equals("")){
                    messageHelper.show("Enter organization name");
                    return;
                }
                if (audienceStrength.s_1.get().equals("")){
                    messageHelper.show("Enter audience strength");
                    return;
                }
                if (dateVm.date.get().equalsIgnoreCase("choose")){
                    messageHelper.show("Please enter a date");
                    return;
                }
                if (hostClassLocation.s_1.get().equals("")){
                    messageHelper.show("Please enter host location");
                    return;
                }
                if (mobileNumber.s_1.get().equals("")){
                    messageHelper.show("Please enter a mobile number");
                    return;
                }

                QuoteReq.Snippet snippet = new QuoteReq.Snippet();
                snippet.setUserId(pref.getString(Constants.BG_ID,""));
                snippet.setClassId(classId);
                snippet.setCatalogueId(catalogueId);
                snippet.setDescriptionYes("");
                snippet.setClassType(TextUtils.join("",classModeVm.getSelectedItemsId()));
                snippet.setOrganization(organizationName.s_1.get());
                snippet.setTotalSeats(audienceStrength.s_1.get());
                snippet.setLocation(hostClassLocation.s_1.get());
                snippet.setDate(dateVm.date.get());
                snippet.setDescription(description.s_1.get());
                snippet.setMobile(mobileNumber.s_1.get());
                snippet.setExplainCatalogueClass(classNeedDetail.s_1.get());
                snippet.setDescription(description.s_1.get());
                uiHelper.popFragment();
                apiService.getQuote(snippet).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                        messageHelper.show(baseResp.getResMsg());


                    }
                });

            }
        };

    }


}
