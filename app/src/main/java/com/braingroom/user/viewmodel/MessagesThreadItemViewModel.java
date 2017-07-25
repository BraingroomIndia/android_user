package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessagesThreadItemViewModel extends ViewModel {

    public final String message, sentDate;
    public boolean isMyMessage;

    public MessagesThreadItemViewModel(@NonNull final boolean isMyMessage,
                                       @NonNull String message, @NonNull String sentDate) {


        this.message = message;
        this.sentDate = getHumanDate(sentDate);
        this.isMyMessage = isMyMessage;
    }

    private String getHumanDate(String timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(Long.valueOf(timeStamp)));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }
}
