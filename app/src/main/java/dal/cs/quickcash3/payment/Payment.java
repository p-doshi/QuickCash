package dal.cs.quickcash3.payment;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

public interface Payment {
    void processPayment();

    void setPaymentAmount(@NonNull String amount);

    String getPaymentAmount();

    void handlePaymentResult(@NonNull ActivityResult result);
}
