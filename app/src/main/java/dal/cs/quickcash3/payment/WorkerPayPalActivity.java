package dal.cs.quickcash3.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;

public class WorkerPayPalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_worker_pay_pal);

        // Initialize UI elements
        Button checkworkerPaymentButton = findViewById(R.id.seePayStatus);

        checkworkerPaymentButton.setOnClickListener(view -> moveToPaymentStatusWindow());
    }

    protected void moveToPaymentStatusWindow() {
        Intent paymentStatusIntent = null;

        paymentStatusIntent = new Intent(getBaseContext(), WorkerPaymentConfirmationActivity.class);

        startActivity(paymentStatusIntent);
    }
}
