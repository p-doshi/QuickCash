package dal.cs.quickcash3.payment;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class EmployerPaymentConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_employer_payment_confirmation);
        init();
    }

    private void init() {
        TextView paymentStatus = findViewById(R.id.transactionStatus);
        TextView payID = findViewById(R.id.PayID);
        Intent intent = getIntent();
        String paymentID = intent.getStringExtra("PAY_ID");
        String status = intent.getStringExtra("STATUS");
        paymentStatus.setText(status);
        payID.setText(paymentID);
    }


}

