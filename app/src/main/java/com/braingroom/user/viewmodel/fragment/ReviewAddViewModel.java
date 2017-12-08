package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

import com.braingroom.user.model.response.ReviewAddResp;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by ashketchup on 8/12/17.
 */

public class ReviewAddViewModel extends ViewModel {
    public ObservableField<String> text = new ObservableField<>("");
    public ObservableInt rating = new ObservableInt(0);
    public String id;
    int reviewType;
    ReviewAddHelper reviewAddHelper;
    public Action onClick = new Action() {
        @Override
        public void run() throws Exception {
            apiService.addReview(reviewType, id, text.get(), rating.get() + "").subscribe(new Consumer<ReviewAddResp>() {
                @Override
                public void accept(ReviewAddResp reviewAddResp) throws Exception {
                    reviewAddHelper.run();
                }
            });
        }
    };

    public void submitReview() {


    }

    public interface ReviewAddHelper {
        void run();
    }

    public ReviewAddViewModel(String id, int reviewType, ReviewAddHelper reviewAddHelper) {
        this.id = id;
        this.reviewType = reviewType;
        this.reviewAddHelper = reviewAddHelper;
    }
}
