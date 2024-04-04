package dal.cs.quickcash3.payment;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

public interface Payment {
    /**
     * Method to initialize payment process.
     */
    void processPayment();

    /**
     * Method to get the payment result from payment gateway.
     *
     * @param result outcome of payment activity
     */
    void handlePaymentResult(@NonNull ActivityResult result);

    /**
     * Method to set the amount of money that employer needs to pay to
     * their worker.
     *
     * @param amount amount of money to be paid to worker
     */
    void setPaymentAmount(@NonNull String amount);

    /**
     * Method to get the amount of money that employer needs to pay to
     * their worker.
     *
     */
    String getPaymentAmount();
}
