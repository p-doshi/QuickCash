package dal.cs.quickcash3.payment;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class WorkerPaymentConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_worker_payment_confirmation);
        init();
    }

    private void init() {
        TextView paymentStatus = findViewById(R.id.workerStatusMessage);
        TextView payID = findViewById(R.id.workerPayID);
        //paymentStatus.setText(status);
        //payID.setText(paymentID);
    }

}
