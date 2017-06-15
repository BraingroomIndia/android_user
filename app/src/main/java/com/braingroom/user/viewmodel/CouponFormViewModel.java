package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;

public class CouponFormViewModel extends ViewModel {
    public final MessageHelper messageHelper;
    public final Navigator navigator;
    public final HelperFactory helperFactory;
    public final List<CouponFormDataViewModel> formDataList = new ArrayList<>();
    public final Action submitClicked, notifyAdapter;

    public CouponFormViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, Action notifyAdapter) {
        this.messageHelper = messageHelper;
        this.navigator = navigator;
        this.helperFactory = helperFactory;
        submitClicked = new Action() {
            @Override
            public void run() throws Exception {

            }
        };
        try {
            addNewFormData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.notifyAdapter = notifyAdapter;
    }

    public void addNewFormData() throws Exception {
        formDataList.add(new CouponFormDataViewModel(messageHelper, navigator, helperFactory, new MyConsumer<CouponFormDataViewModel>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull CouponFormDataViewModel vm) {
                try {
                    removeNewFormData(vm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, formDataList.size()));
        notifyAdapter.run();
    }

    public void removeNewFormData(CouponFormDataViewModel vm) throws Exception {
//        if (formDataList.indexOf(vm) == 0) return;
        boolean status = formDataList.remove(vm);
        notifyAdapter.run();

    }

}
