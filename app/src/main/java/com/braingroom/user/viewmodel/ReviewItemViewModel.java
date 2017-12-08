package com.braingroom.user.viewmodel;

import com.braingroom.user.utils.CommonUtils;

/**
 * Created by ashketchup on 7/12/17.
 */

public class ReviewItemViewModel extends ViewModel {
    public int rating;
    public String title;
    public String text;
    public String timestamp;
    ReviewItemViewModel(int rating,String title,String text,String timestamp,String name){
        this.rating=rating;
        this.title=title;
        this.timestamp= CommonUtils.getHumanDate(timestamp)+"   "+name;
        this.text=text;
    }
}
