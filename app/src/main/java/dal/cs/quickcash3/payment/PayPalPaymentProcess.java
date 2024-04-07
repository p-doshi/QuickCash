package dal.cs.quickcash3.payment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.function.Consumer;

import dal.cs.quickcash3.BuildConfig;
import dal.cs.quickcash3.R;

public class PayPalPaymentProcess implements Payment {
    private static final String TAG = EmployerPayPal.class.getName();
    private final ComponentActivity activity;
    private Consumer<String> successFunction;
    private Consumer<String> errorFunction;
    private final PayPalConfiguration payPalConfig;
    private final ActivityResultLauncher<Intent> activityLauncher;
    private String amount;

    public PayPalPaymentProcess(@NonNull FragmentActivity activity) {
        this.activity = activity;
        payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(BuildConfig.PAYPAL_CLIENT_ID);
        activityLauncher = activity.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handlePaymentResult);
    }

    @Override
    public void processPayment(@NonNull Consumer<String> successFunction, @NonNull Consumer<String> errorFunction) {
        this.successFunction = successFunction;
        this.errorFunction = errorFunction;
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                amount), activity.getResources().getString(R.string.currency_cad), activity.getResources().getString(R.string.services), PayPalPayment.PAYMENT_INTENT_SALE);

        // Create Paypal Payment activity intent
        final Intent intent = new Intent(activity, PaymentActivity.class);
        // Adding paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        // Adding paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        // Starting Activity Request launcher
        activityLauncher.launch(intent);
    }

    public void handlePaymentResult(@NonNull ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            assert result.getData() != null;
            //noinspection RedundantSuppression
            @SuppressWarnings({"unchecked", "deprecation"})
            final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirmation != null) {
                try {
                    // Get the payment details
                    JSONObject payObj = confirmation.toJSONObject();
                    Log.v(TAG, payObj.toString(4));
                    Log.v(TAG, payObj.toString(4));
                    // Extract json response and display it in a text view.
                    String payID = payObj.getJSONObject("response").getString("id");
                    String state = payObj.getJSONObject("response").getString("state");
                    if (state.equals(activity.getString(R.string.approved))) {
                        successFunction.accept(payID);
                    } else {
                        errorFunction.accept(state);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "An extremely unlikely failure occurred: ", e);
                    errorFunction.accept(e.getMessage());
                }
            }
        } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
            errorFunction.accept("Launcher Result Invalid");
        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
            errorFunction.accept(activity.getString(R.string.payment_cancelled));
        }
    }

    @Override
    public void setPaymentAmount(@NonNull String amount) {
        this.amount = amount;
    }

    @NonNull
    @Override
    public String getPaymentAmount() {
        return this.amount;
    }
}
