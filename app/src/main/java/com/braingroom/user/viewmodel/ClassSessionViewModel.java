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

    public boolean checked;
    public String name;
    public String OfferPrice;
    public String ActualPrice;
    public String Description;

    public ClassSessionViewModel(String sessionName, String offerPrice, String actualPrice,
                                   String sessionDescription, String priceSymbolNonSpanned ) {

        this.sName.set(sessionName);
        this.sOfferPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + offerPrice));
        this.sActualPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + actualPrice));
        this.sDescription.set(sessionDescription);

        this.name.set();
        this.OfferPrice.set(offerPrice);
        this.ActualPrice.set(actualPrice);
        this.Description.set(sessionDescription);


    }

    public boolean isChecked() {
        return checked;
    }
}
