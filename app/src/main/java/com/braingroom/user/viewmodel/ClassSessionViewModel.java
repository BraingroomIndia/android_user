package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.Spanned;

import com.braingroom.user.model.dto.ClassSession;
import com.braingroom.user.utils.CommonUtils;

import javax.annotation.Nullable;

import io.reactivex.functions.Action;

public class ClassSessionViewModel extends ViewModel
{

    @NonNull
    public final ClassSession classSession;

    @Nullable
    public final Action onClicked;

    public final ObservableField<Spanned> offerPrice = new ObservableField<>();
    public final ObservableField<Spanned> actualPrice = new ObservableField<>();

    public ClassSessionViewModel(@NonNull final ClassSession classSession,Action onClicked, boolean isSelected, String priceSymbolNonSpanned){
        this.classSession=classSession;
        this.onClicked=onClicked;
        this.actualPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + classSession.getPrice()));
        this.offerPrice.set(CommonUtils.fromHtml(priceSymbolNonSpanned + classSession.getOfferPrice()));
        classSession.setSelected(isSelected);

    }
}
