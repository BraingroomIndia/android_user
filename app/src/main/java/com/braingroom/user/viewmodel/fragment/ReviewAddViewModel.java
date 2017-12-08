package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

/**
 * Created by ashketchup on 8/12/17.
 */

public class ReviewAddViewModel extends ViewModel {
    public ObservableField<String> text;
    public ObservableInt rating;
    public String id;
    int reviewType;
    ReviewAddHelper reviewAddHelper;
    public Action onClick = new Action() {
        @Override
        public void run() throws Exception {
            submitReview();
        }
    };
    public void submitReview(){
        apiService.addReview(reviewType,id,text.get(),""+rating.get());
        if(reviewAddHelper!=null)
            reviewAddHelper.run();
    }
    public interface ReviewAddHelper {
        void run();
    }
    public ReviewAddViewModel(String id,int reviewType,ReviewAddHelper reviewAddHelper){
        this.id=id;
        this.reviewType=reviewType;
        this.reviewAddHelper = reviewAddHelper;
    }
}
