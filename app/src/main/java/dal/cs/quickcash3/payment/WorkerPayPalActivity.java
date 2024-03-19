package dal.cs.quickcash3.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;

public class WorkerPayPalActivity extends AppCompatActivity {
    private final CompletedJob job = new CompletedJob();
    private static final String LOG_TAG = WorkerPayPalActivity.class.getSimpleName();
    private final AtomicBoolean buttonWaiting = new AtomicBoolean(false);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_worker_pay_pal);

        // Initialize UI elements
        Button checkworkerPaymentButton = findViewById(R.id.seePayStatus);
        Database database = new MyFirebaseDatabase();
        job.readFromDatabase(database,
                "kawnerv9823fh",
                () -> {
                    if (buttonWaiting.get()) {
                        moveToPaymentStatusWindow();
                    }
                },
                error -> Log.w(LOG_TAG, error));
        checkworkerPaymentButton.setOnClickListener(view -> {
            if (job.getPayId() == null) {
                buttonWaiting.set(true);
            } else {
                moveToPaymentStatusWindow();
            }
        });
    }

    protected void moveToPaymentStatusWindow() {
        Intent paymentStatusIntent = null;

        paymentStatusIntent = new Intent(getBaseContext(), WorkerPaymentConfirmationActivity.class);
        paymentStatusIntent.putExtra("PayID", job.getPayId());
        paymentStatusIntent.putExtra("Status", job.getStatus());

        startActivity(paymentStatusIntent);
    }
}
