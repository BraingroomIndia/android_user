package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.PostDetailActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;

public class NotificationsItemViewModel extends ViewModel {

    public final Action onClicked;
    public final String title, postId, classId;
    public final boolean readStatus;

    public NotificationsItemViewModel(@NonNull final Navigator navigator,
                                      @NonNull final String title, @NonNull final String postId,
                                      @NonNull String classId, @NonNull boolean readStatus) {

        this.title = title;
        this.postId = postId;
        this.classId = classId;
        this.readStatus = readStatus;

        onClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putString("postId", postId);
                navigator.navigateActivity(PostDetailActivity.class, data);

            }
        };

    }

    private String getHumanDate(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
}
