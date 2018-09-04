package com.braingroom.user.viewmodel;

//import android.databinding.ObservableField;
import android.support.annotation.NonNull;
//import android.text.Spanned;

import com.braingroom.user.model.dto.ClassSession;
//import com.braingroom.user.utils.CommonUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.functions.Action;

public class ClassSessionViewModel extends ViewModel
{

    @NonNull
    public final ClassSession classSession;
    @Nullable
    public final Action onClicked;

    public ClassSessionViewModel(@NonNull final ClassSession classSession,Action onClicked){
        this.classSession=classSession;
        this.onClicked=onClicked;
        classSession.setSelected(false);
    }
}

//    public ClassSessionViewModel(String sessionName, String offerPrice, String price, String sessionDesc, String priceSymbolNonSpanned) {
//    }









//    public final ObservableField<String> sName = new ObservableField<>();
//    public final ObservableField<Spanned> sOfferPrice = new ObservableField<>();
//    public final ObservableField<Spanned> sActualPrice = new ObservableField<>();
//    public final ObservableField<String> sDescription = new ObservableField<>();

//    public boolean checked;
//    public String name;
//    public String OfferPrice;
//    public String ActualPrice;
//    public String Description;

//    public ClassSessionViewModel(String sessionName, String offerPrice, String actualPrice,
//                                   String sessionDescription, String priceSymbolNonSpanned ) {

//        this.sName.set(sessionName);
//        this.sOfferPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + offerPrice));
//        this.sActualPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + actualPrice));
//        this.sDescription.set(sessionDescription);

//        this.name.set(sessionName);
//        this.OfferPrice.set(offerPrice);
//        this.ActualPrice.set(actualPrice);
//        this.Description.set(sessionDescription);
//    this.name=name;
//        this.OfferPrice=offerPrice;
//        this.ActualPrice=actualPrice;
//        this.Description=sessionDescription;
//    public boolean isChecked() {
//        return checked;
//    }
//}



