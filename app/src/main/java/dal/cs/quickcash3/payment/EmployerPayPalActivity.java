package dal.cs.quickcash3.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.BuildConfig;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import dal.cs.quickcash3.R;

public class EmployerPayPalActivity extends AppCompatActivity {
    private static final String TAG = EmployerPayPalActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    Button employerPayConfirmationButton;
    String payID;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_employer_pay_pal);
        init();
        configPayPal();
        initActivityLauncher();
        setListeners();
    }

    private void init() {
        employerPayConfirmationButton = findViewById(R.id.employerPayConfirmationButton);
    }

    private void configPayPal() {
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
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
                                moveToConfirmPaymentWindow();
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


    private void setListeners() {
        employerPayConfirmationButton.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        final String amount = "50";
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                amount), getResources().getString(R.string.currency_cad), getResources().getString(R.string.services), PayPalPayment.PAYMENT_INTENT_SALE);

        // Create Paypal Payment activity intent
        final Intent intent = new Intent(this, PaymentActivity.class);
        // Adding paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        // Adding paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        // Starting Activity Request launcher
        activityResultLauncher.launch(intent);
    }

    protected void moveToConfirmPaymentWindow() {
        Intent paymentConfirmationIntent;

        paymentConfirmationIntent = new Intent(getBaseContext(), EmployerPaymentConfirmationActivity.class);

        paymentConfirmationIntent.putExtra("PAY_ID", payID);
        paymentConfirmationIntent.putExtra("STATUS", state);

        startActivity(paymentConfirmationIntent);
    }

//    protected void writeToDatabase () {
//
//    }
}

