package dal.cs.quickcash3.payment;

import static dal.cs.quickcash3.test.ExampleJobList.COMPLETED_JOB1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;

public class WorkerCheckPayment extends Fragment {
    private static final String LOG_TAG = WorkerCheckPayment.class.getSimpleName();
    private final Database database;

    public WorkerCheckPayment(Database database) {
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View workerCheckStatusView = inflater.inflate(R.layout.fragment_worker_check_payment, container, false);
        super.onCreate(savedInstanceState);

        Button checkWorkerPaymentButton = workerCheckStatusView.findViewById(R.id.checkPayStatus);

        checkWorkerPaymentButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "pressed worker check payment button");
            handleButton();
        });

        return workerCheckStatusView;
    }

    private void handleButton() {
        CompletedJob.readFromDatabase(
                database,
                COMPLETED_JOB1,
                this::moveToPaymentStatusWindow,
                error -> Log.w(LOG_TAG, error));
    }

    private void moveToPaymentStatusWindow(@NonNull CompletedJob job) {
        Context context = getContext();
        Intent paymentStatusIntent;

        paymentStatusIntent = new Intent(context, PaymentConfirmationActivity.class);
        paymentStatusIntent.putExtra(getString(R.string.PAY_ID), job.getPayId());

        startActivity(paymentStatusIntent);
    }
}
