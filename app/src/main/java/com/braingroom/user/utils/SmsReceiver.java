package com.braingroom.user.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.braingroom.user.UserApplication;

/**
 * Created by godara on 01/07/17.
 */


public class SmsReceiver extends BroadcastReceiver {
    //TODO change sms origin key
    public static final String SMS_ORIGIN = "ANHIVE";
    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
    static String message;
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                if (pdusObj != null)
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String senderAddress = currentMessage.getDisplayOriginatingAddress();
                        message = currentMessage.getDisplayMessageBody();
                        Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
                        // if the SMS is not from our gateway, ignore the message
                        if (!senderAddress.toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                            return;
                        } else {
                            UserApplication.getInstance().getOtpArrived().onNext(true);
                            return;
                        }
                    }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     *      * Getting the OTP from sms message body
     *      * ':' is the separator of OTP from the message
     *      *
     *      * @param message
     *      * @return
     *      
     */
    public static String getVerificationCode() {
        String code;
        int index = message.indexOf(OTP_DELIMITER);
        if (index != -1) {
            int start = index + 2;
            int length = 6;
            code = message.substring(start, start + length);
            Log.e(TAG, "OTP received: " + code);
            return code;
        }
        return null;
    }
}
