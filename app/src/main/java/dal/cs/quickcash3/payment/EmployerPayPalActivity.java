package dal.cs.quickcash3.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class EmployerPayPalActivity extends AppCompatActivity {
    PayPalPaymentProcess paymentProcess;
    Button employerPayConfirmationButton;

    TextView paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_employer_pay_pal);
        paymentProcess = new PayPalPaymentProcess(this);
        paymentProcess.setPaymentAmount("40");
        init();
        //paymentProcess.configPayPal();
        //paymentProcess.initActivityLauncher();
        setListeners();
    }

    private void init() {
        employerPayConfirmationButton = findViewById(R.id.employerPayConfirmationButton);
        paymentStatus = findViewById(R.id.AmountNumText);
        paymentStatus.setText(paymentProcess.getPaymentAmount());
    }

    private void setListeners() {
        employerPayConfirmationButton.setOnClickListener(v -> paymentProcess.processPayment());
    }

    protected void moveToConfirmPaymentWindow(String payID, String state) {
        Intent paymentConfirmationIntent;

        paymentConfirmationIntent = new Intent(getBaseContext(), EmployerPaymentConfirmationActivity.class);

        paymentConfirmationIntent.putExtra("PAY_ID", payID);
        paymentConfirmationIntent.putExtra("STATUS", state);

        startActivity(paymentConfirmationIntent);
    }
}

