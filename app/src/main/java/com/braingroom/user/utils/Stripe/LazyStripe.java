package com.braingroom.user.utils.Stripe;

import android.content.Context;
import android.support.annotation.NonNull;

import com.braingroom.user.view.activity.StripeActivity;
import com.stripe.android.Stripe;

/**
 * Created by godara on 01/02/18.
 */

public class LazyStripe {

    private Stripe stripe;
    private final Context context;
    private final String publishableKey;

    public LazyStripe(@NonNull Context context, String publishableKey) {
        this.context = context;
        this.publishableKey = publishableKey;
    }

    public  synchronized Stripe getStripe() {
        if (stripe == null)
            stripe = new Stripe(context, publishableKey);
        return stripe;
    }
}
