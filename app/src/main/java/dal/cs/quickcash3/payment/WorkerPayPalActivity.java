package dal.cs.quickcash3.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.util.AsyncLatch;

public class WorkerPayPalActivity extends AppCompatActivity {
    private static final String LOG_TAG = WorkerPayPalActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_worker_pay_pal);

        // Initialize UI elements
        Button checkWorkerPaymentButton = findViewById(R.id.seePayStatus);
        Database database = new MyFirebaseDatabase();

        AsyncLatch<CompletedJob> asyncJob = new AsyncLatch<>();

        CompletedJob.readFromDatabase(
            database,
            "kawnerv9823fh",
            asyncJob::set,
            error -> Log.w(LOG_TAG, error));
        checkWorkerPaymentButton.setOnClickListener(view ->
            asyncJob.get(this::moveToPaymentStatusWindow));
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private void moveToPaymentStatusWindow(@NonNull CompletedJob job) {
        Intent paymentStatusIntent;

        paymentStatusIntent = new Intent(getBaseContext(), WorkerPaymentConfirmationActivity.class);
        paymentStatusIntent.putExtra("PayID", job.getPayId());
        paymentStatusIntent.putExtra("Status", job.getStatus());

        startActivity(paymentStatusIntent);
    }
}
