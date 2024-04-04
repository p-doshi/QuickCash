package dal.cs.quickcash3.payment;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class WorkerPaymentConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_worker_payment_confirmation);
        init();
    }

    private void init() {
        TextView payID = findViewById(R.id.workerPayID);
        Intent intent = getIntent();
        String paymentID = intent.getStringExtra("PayID");
        payID.setText(paymentID);
    }

}
