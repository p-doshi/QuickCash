package dal.cs.quickcash3.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.employer.PostJobForm;
import dal.cs.quickcash3.geocode.MyGeocoder;

import androidx.fragment.app.Fragment;

public class EmployerPayPal extends Fragment {
    private static final String LOG_TAG = EmployerPayPal.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View employerPayPalView = inflater.inflate(R.layout.employer_pay_pal, container, false);
        Context context = getContext();
        super.onCreate(savedInstanceState);

        PayPalPaymentProcess paymentProcess = new PayPalPaymentProcess(requireActivity(), this::moveToConfirmPaymentWindow);
        paymentProcess.setPaymentAmount("40");

        Button employerPayConfirmationButton = employerPayPalView.findViewById(R.id.employerPayConfirmationButton);
        TextView paymentStatus = employerPayPalView.findViewById(R.id.AmountNumText);

        employerPayConfirmationButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "pressed button");
            paymentProcess.processPayment();
        });
        paymentStatus.setText(paymentProcess.getPaymentAmount());

        return employerPayPalView;
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private void moveToConfirmPaymentWindow(@NonNull String payID, @NonNull String state) {
        Intent paymentConfirmationIntent;

        paymentConfirmationIntent = new Intent(requireContext(), EmployerPaymentConfirmationActivity.class);

        paymentConfirmationIntent.putExtra("PAY_ID", payID);
        paymentConfirmationIntent.putExtra("STATUS", state);

        startActivity(paymentConfirmationIntent);
    }
}
