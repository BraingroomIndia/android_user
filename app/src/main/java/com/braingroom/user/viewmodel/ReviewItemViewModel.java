package com.braingroom.user.viewmodel;

import com.braingroom.user.utils.CommonUtils;

/**
 * Created by ashketchup on 7/12/17.
 */

public class ReviewItemViewModel extends ViewModel {
    public final String rating;
    public final String title;
    public final String text;
    public final String timestamp;
    public final String className;

    public ReviewItemViewModel(int rating, String title, String text, String timestamp) {
        this.rating = rating + "";
        this.title = title;
        this.timestamp = timestamp;
        this.text = text;
        className = null;
    }

    public ReviewItemViewModel(int rating, String title, String className, String text, String timestamp) {
        this.rating = rating + "";
        this.title = title;
        this.timestamp = timestamp;
        this.text = text;
        this.className = className;
    }
}
