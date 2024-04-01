package dal.cs.quickcash3.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class EmployerPayPalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_employer_pay_pal);

        PayPalPaymentProcess paymentProcess = new PayPalPaymentProcess(this, this::moveToConfirmPaymentWindow);
        paymentProcess.setPaymentAmount("40");

        Button employerPayConfirmationButton = findViewById(R.id.employerPayConfirmationButton);
        TextView paymentStatus = findViewById(R.id.AmountNumText);

        employerPayConfirmationButton.setOnClickListener(v -> {
            Log.d("test", "pressed button");
            paymentProcess.processPayment();
        });
        paymentStatus.setText(paymentProcess.getPaymentAmount());
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private void moveToConfirmPaymentWindow(@NonNull String payID, @NonNull String state) {
        Intent paymentConfirmationIntent;

        paymentConfirmationIntent = new Intent(getBaseContext(), EmployerPaymentConfirmationActivity.class);

        paymentConfirmationIntent.putExtra("PAY_ID", payID);
        paymentConfirmationIntent.putExtra("STATUS", state);

        startActivity(paymentConfirmationIntent);
    }
}
