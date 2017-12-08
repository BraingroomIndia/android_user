package com.braingroom.user.viewmodel.fragment;

import android.databinding.Observable;
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
    public Action onClick = new Action() {
        @Override
        public void run() throws Exception {
            submitReview();
        }
    };
    public void submitReview(){
        apiService.addReview(reviewType,id,text.get(),""+rating.get());
    }
    public ReviewAddViewModel(String id,int reviewType){
        this.id=id;
        this.reviewType=reviewType;
    }
}
