package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.model.response.ReviewAddResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by ashketchup on 8/12/17.
 */

public class ReviewAddViewModel extends ViewModel {
    public ObservableField<String> text = new ObservableField<>("");
    public ObservableInt rating = new ObservableInt(0);
    public String id;
    private final int reviewType;
    public final MessageHelper messageHelper;
    ReviewAddHelper reviewAddHelper;
    public Action onClick = new Action() {
        @Override
        public void run() throws Exception {
            if (rating.get() == 0) {
                messageHelper.show("Please rate the class between 1-5");
                return;
            }
            apiService.addReview(reviewType, id, text.get(), rating.get() + "").observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ReviewAddResp>() {
                @Override
                public void accept(final ReviewAddResp reviewAddResp) throws Exception {
                    messageHelper.showAcceptableInfo("Submitted", reviewAddResp.getResMsg(), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            reviewAddHelper.run();
                        }
                    });
                }
            });
        }
    };

    public interface ReviewAddHelper {
        void run();
    }

    public ReviewAddViewModel(MessageHelper messageHelper, String id, int reviewType, ReviewAddHelper reviewAddHelper) {
        this.id = id;
        this.reviewType = reviewType;
        this.reviewAddHelper = reviewAddHelper;
        this.messageHelper = messageHelper;
    }
}
