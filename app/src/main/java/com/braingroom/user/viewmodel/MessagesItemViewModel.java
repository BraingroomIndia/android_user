package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CatalogueCheckOutActivity;
import com.braingroom.user.view.activity.CatalogueHomeActivity;
import com.braingroom.user.view.activity.MessagesThreadActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;

public class MessagesItemViewModel extends ViewModel {

    public final Action onClicked;
    public final String senderImage, senderName, message, sentDate;

    public MessagesItemViewModel(@NonNull final Navigator navigator, @NonNull final int messageType, @NonNull final String quoteId, @NonNull final String senderId,
                                 @NonNull final String senderName, @NonNull final String senderImage,
                                 @NonNull String message, @NonNull String sentDate) {

        this.senderImage = senderImage;
        this.senderName = senderName;
        this.message = message;
        this.sentDate = sentDate;
        onClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                if (messageType == 1) {
                    data.putString(Constants.quoteId, quoteId);
                    navigator.navigateActivity(CatalogueCheckOutActivity.class, data);
                } else {
                    data.putString("sender_id", senderId);//senderId);
                    data.putString("sender_name", senderName);//senderId);
                    data.putString("sender_image", senderImage);//senderId);
                    navigator.navigateActivity(MessagesThreadActivity.class, data);
                }
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
