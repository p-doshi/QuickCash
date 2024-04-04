package dal.cs.quickcash3.payment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;

public class EmployerPayPal extends Fragment {
    private static final String LOG_TAG = EmployerPayPal.class.getSimpleName();

    private final Payment payment;

    public EmployerPayPal(@NonNull Payment payment) {
        this.payment = payment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View employerPayPalView = inflater.inflate(R.layout.fragment_employer_pay_pal, container, false);
        super.onCreate(savedInstanceState);

        payment.setPaymentAmount("40");

        Button employerPayConfirmationButton = employerPayPalView.findViewById(R.id.employerPayConfirmationButton);
        TextView paymentStatus = employerPayPalView.findViewById(R.id.AmountNumText);

        employerPayConfirmationButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "pressed button");
            payment.processPayment();
        });
        paymentStatus.setText(payment.getPaymentAmount());

        return employerPayPalView;
    }
}
