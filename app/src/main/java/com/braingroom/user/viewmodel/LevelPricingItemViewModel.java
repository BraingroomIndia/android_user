package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.view.MessageHelper;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class LevelPricingItemViewModel extends ViewModel {

    @NonNull
    public final ObservableField<String> levelName;

    @NonNull
    public final ObservableField<String> sublevelName;

    @NonNull
    public final ObservableField<Integer> totalPrice;

    @NonNull
    public final CountModifierItemViewModel countVm;

    public final String levelId;
    /*
    * for per person only one element thats fixed for all
    * for group list of size till last change like 1-3 = Rs. 100  and 4-5 = Rs. 50 and Rs 30 for 5+
    * priceList = [100,100,100,50,50]*/
    public final List<Integer> priceList;

    public final Action dataChangeAction, sublevelClicked;
    public Consumer<Integer> countConsumer = new Consumer<Integer>() {
        @Override
        public void accept(@io.reactivex.annotations.NonNull Integer count) throws Exception {
            // per person pricing
            if (priceList.size() == 1) {
                totalPrice.set(count * priceList.get(0));
            } else {
                // group pricing
                int totalPrice1 = 0;
                for (int i = 0; i < count; i++) {
                    totalPrice1 = totalPrice1 + priceList.get(i);
                }
                int totalPrice2 = 0;
                if (count > priceList.size()) {
                    totalPrice2 = priceList.get(priceList.size() - 1) * (count - priceList.size());
                }
                totalPrice.set(totalPrice1 + totalPrice2);
            }
            dataChangeAction.run();
        }
    };

    public LevelPricingItemViewModel(final String levelName, String levelId, final String subLevelName, final String sublevelDesc, List<Integer> priceList, Action dataChangeAction, @NonNull final MessageHelper messageHelper) {
        this.levelName = new ObservableField<>(levelName);
        this.levelId = levelId;
        this.sublevelName = new ObservableField<>(subLevelName);
        totalPrice = new ObservableField<>(0);
        this.priceList = priceList;
        this.dataChangeAction = dataChangeAction;
        countVm = new CountModifierItemViewModel(1, countConsumer);
        sublevelClicked = new Action() {
            @Override
            public void run() throws Exception {
                messageHelper.showDismissInfo(null, sublevelDesc);

            }
        };

    }


}
