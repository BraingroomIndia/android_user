package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.text.Spanned;
import com.braingroom.user.utils.CommonUtils;

public class ClassSessionViewModel extends ViewModel
{
    public final ObservableField<String> sName = new ObservableField<>();
    public final ObservableField<Spanned> sOfferPrice = new ObservableField<>();
    public final ObservableField<Spanned> sActualPrice = new ObservableField<>();
    public final ObservableField<String> sDescription = new ObservableField<>();

    public ClassSessionViewModel(String sessionName, String offerPrice, String actualPrice,
                                   String sessionDescription, String priceSymbolNonSpanned ) {

        this.sName.set(sessionName);
        this.sOfferPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + offerPrice));
        this.sActualPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + actualPrice));
        this.sDescription.set(sessionDescription);

    }
}
