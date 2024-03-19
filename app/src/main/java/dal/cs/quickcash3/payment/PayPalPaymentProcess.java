package dal.cs.quickcash3.payment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Objects;

import dal.cs.quickcash3.BuildConfig;
import dal.cs.quickcash3.R;

public class PayPalPaymentProcess {
    protected ActivityResultLauncher<Intent> activityResultLauncher;
    private static final String TAG = EmployerPayPalActivity.class.getName();
    protected PayPalConfiguration payPalConfig;
    private final EmployerPayPalActivity activity;
    private String payID;
    private String state;
    private String amount;

    public PayPalPaymentProcess(@NonNull EmployerPayPalActivity activity) {
        this.activity = activity;
        configPayPal();
        initActivityLauncher();
    }

    protected void configPayPal() {
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    }

    protected void processPayment() {
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                amount), activity.getResources().getString(R.string.currency_cad), activity.getResources().getString(R.string.services), PayPalPayment.PAYMENT_INTENT_SALE);

        // Create Paypal Payment activity intent
        final Intent intent = new Intent(activity, PaymentActivity.class);
        // Adding paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        // Adding paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        // Starting Activity Request launcher
        activityResultLauncher.launch(intent);
    }

    protected void initActivityLauncher() {
        activityResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        //noinspection RedundantSuppression
                        @SuppressWarnings({"unchecked", "deprecation"})
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (confirmation != null) {
                            try {
                                // Get the payment details
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                Log.i(TAG, paymentDetails);
                                // Extract json response and display it in a text view.
                                JSONObject payObj = new JSONObject(paymentDetails);
                                payID = payObj.getJSONObject("response").getString("id");
                                state = payObj.getJSONObject("response").getString("state");
                                activity.moveToConfirmPaymentWindow(payID, state);
                            } catch (JSONException e) {
                                Log.e("Error", "an extremely unlikely failure occurred: ", e);
                            }
                        }
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Log.d(TAG, "Launcher Result Invalid");
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Log.d(TAG, "Launcher Result Cancelled");
                    }
                });
    }

    protected void setPaymentAmount(@NonNull String amount) {
        this.amount = amount;
    }

    protected @Nullable String getPaymentAmount() {
        return this.amount;
    }
}
