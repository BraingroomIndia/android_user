package com.braingroom.user.view.activity;

import android.content.ContentProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.utils.CustomView.CardView.OnCardFormSubmitListener;
import com.braingroom.user.utils.CustomView.CardView.utils.CardType;
import com.braingroom.user.utils.CustomView.CardView.view.CardEditText;
import com.braingroom.user.utils.CustomView.CardView.view.CardForm;
import com.braingroom.user.utils.CustomView.CardView.view.SupportedCardTypesView;
import com.braingroom.user.utils.Stripe.LazyStripe;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.stripe.android.CustomerSession;
import com.stripe.android.SourceCallback;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Customer;
import com.stripe.android.model.CustomerSource;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import timber.log.Timber;

/*
 * Created by godara on 31/01/18.
 */

public class StripeActivity extends AppCompatActivity implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener {

    private static final CardType[] SUPPORTED_CARD_TYPES = {CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY};

    private SupportedCardTypesView mSupportedCardTypesView;
    private String TAG = StripeActivity.class.getSimpleName();

    protected CardForm mCardForm;
    private Source mRedirectSource;

    private static String PUBLISHABLE_KEY = "";

    public static final String RETURN_SCHEMA = "braingroom://";
    public static final String RETURN_HOST_ASYNC = "stripe";
    public static final String RETURN_HOST_SYNC = "sync";
    public static final String QUERY_CLIENT_SECRET = "client_secret";
    public static final String QUERY_SOURCE_ID = "source";
    private LazyStripe mStripe;
    private JSONObject checkout;
    private int checkoutAmount;
    private String checkOutCurrency;
    protected Bundle extras;
    MaterialDialog progressDialog;

    private SourceParams sourceParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        PUBLISHABLE_KEY = getString(R.string.stripe_publish_key);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_stripe);
        mStripe = new LazyStripe(this, PUBLISHABLE_KEY);
        mSupportedCardTypesView = findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras().getBundle("classData");
            try {
                checkout = new JSONObject(extras.getString("checkoutData"));
                checkoutAmount = Integer.parseInt(checkout.getString("amount"));
                checkOutCurrency = checkout.getString("currency");
            } catch (JSONException e) {
                e.printStackTrace();
                this.setResult(android.app.Activity.RESULT_CANCELED);
                finish();


            }
        }

        mCardForm = findViewById(R.id.card_form);
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel(getString(R.string.purchase))
                .setup(this);
        mCardForm.setOnCardFormSubmitListener(this);
        mCardForm.setOnCardTypeChangedListener(this);

        // Warning: this is for development purposes only and should never be done outside of this example app.
        // Failure to set FLAG_SECURE exposes your app to screenshots allowing other apps to steal card information.
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }

    @Override
    public void onCardFormSubmit() {

        final Card card = new Card(mCardForm.getCardNumber(), mCardForm.getExpirationMonth(), mCardForm.getExpirationYear(), mCardForm.getCvv());

        if (card.validateCard()) {
            showProgressDialog("Wait", "Communicating with server");
            sourceParams = SourceParams.createCardParams(card);
            attemptPurchase();
        } else {
            mCardForm.validate();
            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        return true;

    }

    private void createSource(SourceParams cardSourceParams, Stripe mStripe) {
        mStripe.createSource(
                cardSourceParams,
                new SourceCallback() {
                    @Override
                    public void onSuccess(Source source) {
                        source.getId();
                    }

                    @Override
                    public void onError(Exception error) {
                        // Tell the user that something went wrong
                    }
                });
    }

    private void attemptPurchase() {
        mStripe.getStripe().createSource(sourceParams, new SourceCallback() {
            @Override
            public void onError(Exception error) {
                dismissActiveProgress();
                Timber.tag("Stripe").e(error, error.getMessage());
// TODO  displayError("No payment method selected");
            }

            @Override
            public void onSuccess(Source source) {

                proceedWithPurchaseIf3DSCheckIsNotNecessary(source);
            }
        });

       /* CustomerSession.getInstance().retrieveCurrentCustomer(new CustomerSession.CustomerRetrievalListener() {
            @Override
            public void onCustomerRetrieved(@NonNull Customer customer) {
                String sourceId = customer.getDefaultSource();
                if (sourceId == null) {
//                  TODO  displayError("No payment method selected");
                    return;
                }
                CustomerSource source = customer.getSourceById(sourceId);
                proceedWithPurchaseIf3DSCheckIsNotNecessary(source.asSource(), customer.getId());
            }

            @Override
            public void onError(int errorCode, @Nullable String errorMessage) {
//         TODO       displayError("Error getting payment method");
            }
        });*/

    }

    private void proceedWithPurchaseIf3DSCheckIsNotNecessary(Source source) {
        if (source == null || !Source.CARD.equals(source.getType())) {
//  TODO          displayError("Something went wrong - this should be rare");
            return;
        }

        SourceCardData cardData = (SourceCardData) source.getSourceTypeModel();
        if (SourceCardData.REQUIRED.equals(cardData.getThreeDSecureStatus())) {
            // In this case, you would need to ask the user to verify the purchase.
            createThreeDSecureSource(source.getId());
        } else {
            // If 3DS is not required, you can charge the source.
            Intent resultIntent = new Intent();
            resultIntent.putExtra("stripe_token", source.getId());
            setResult(RESULT_OK, resultIntent);
            dismissActiveProgress();
            finish();
        }
    }

    private void createThreeDSecureSource(String cardSourceId) {

        SourceParams threeDParams = SourceParams.createThreeDSecureParams(
                checkoutAmount, // some price: this represents 10.00 EUR
                checkOutCurrency, // a currency
                getUrl(false), // your redirect
                cardSourceId);

        mStripe.getStripe().createSource(threeDParams, new SourceCallback() {
            @Override
            public void onError(Exception error) {
                Timber.tag("Stripe").e(error, error.getMessage());
            }

            @Override
            public void onSuccess(Source source) {
                showDialog(source);
            }
        });
    }

    private static String getUrl(boolean isSync) {
        if (isSync) {
            return RETURN_SCHEMA + RETURN_HOST_SYNC;
        } else {
            return RETURN_SCHEMA + RETURN_HOST_ASYNC;
        }
    }

    void showDialog(final Source source) {
        // Caching the source object here because this app makes a lot of them.
        dismissActiveProgress();
        mRedirectSource = source;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(source.getRedirect().getUrl()));
        this.startActivity(browserIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getData() != null && intent.getData().getQuery() != null) {
            // The client secret and source ID found here is identical to
            // that of the source used to get the redirect URL.
            String clientSecret = intent.getData().getQueryParameter(QUERY_CLIENT_SECRET);
            String sourceId = intent.getData().getQueryParameter(QUERY_SOURCE_ID);
            if (clientSecret != null
                    && sourceId != null
                    && clientSecret.equals(mRedirectSource.getClientSecret())
                    && sourceId.equals(mRedirectSource.getId())) {
                Log.e(TAG, sourceId);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("stripe_token", sourceId);
                setResult(RESULT_OK, resultIntent);
                finish();
                mRedirectSource = null;
            } else Timber.tag("Stripe").wtf("Wtf went wrong");
        } else Timber.tag("Stripe").wtf("Wtf went wrong");
    }

    public void showProgressDialog(@NonNull String title, @NonNull String content) {
        dismissActiveProgress();
        progressDialog = new MaterialDialog.Builder(StripeActivity.this)
                .title(title)
                .content(content)
                .progress(true, 0).cancelable(false)
//                .canceledOnTouchOutside(false)
                .show();

    }

    public void dismissActiveProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


}
