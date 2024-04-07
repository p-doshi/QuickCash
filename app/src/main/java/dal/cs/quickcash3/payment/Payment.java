package dal.cs.quickcash3.payment;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface Payment {
    /**
     * Method to initialize payment process.
     */
    void processPayment(@NonNull Consumer<String> successFunction, @NonNull Consumer<String> errorFunction);

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
    @NonNull String getPaymentAmount();
}
