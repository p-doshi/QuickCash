package dal.cs.quickcash3.payment;

import static dal.cs.quickcash3.test.ExampleJobList.COMPLETED_JOB1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;

public class WorkerPayPalActivity extends AppCompatActivity {
    private static final String LOG_TAG = WorkerPayPalActivity.class.getSimpleName();
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_worker_pay_pal);

        initInterfaces();

        // Initialize UI elements
        Button checkWorkerPaymentButton = findViewById(R.id.seePayStatus);
        checkWorkerPaymentButton.setOnClickListener(view -> handleButton());
    }

    private void handleButton() {
        CompletedJob.readFromDatabase(
            database,
            COMPLETED_JOB1,
            this::moveToPaymentStatusWindow,
            error -> Log.w(LOG_TAG, error));
    }

    private void moveToPaymentStatusWindow(@NonNull CompletedJob job) {
        Intent paymentStatusIntent;

        paymentStatusIntent = new Intent(getBaseContext(), WorkerPaymentConfirmationActivity.class);
        paymentStatusIntent.putExtra("PayID", job.getPayId());

        startActivity(paymentStatusIntent);
    }

    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.i(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }
    }

    public @NonNull Database getDatabase(){
        return database;
    }
}
