package dal.cs.quickcash3.payment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class PaymentConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_payment_confirmation);

        TextView payID = findViewById(R.id.payID);
        String paymentID = getIntent().getStringExtra(getString(R.string.PAY_ID));
        payID.setText(paymentID);
    }
}
