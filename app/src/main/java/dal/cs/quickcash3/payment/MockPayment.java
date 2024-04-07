package dal.cs.quickcash3.payment;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

/**
 * MockPayment class that represents a mock version of payment gateway.
 */
public class MockPayment implements Payment {
    private String amount;
    private String message;
    private boolean success;

    /**
     * Method to set payment result to success by assigning Pay id to message.
     *
     * @param message Pay ID of the payment that was made by the employer
     */
    public void setSuccess (String message) {
        this.message = message;
        success = true;
    }

    /**
     * Method to set payment result to failure by assigning error message to message.
     *
     * @param message error message given by the payment gateway
     */
    public void setFailure (String message) {
        this.message = message;
        success = false;
    }

    @Override
    public void processPayment(Consumer<String> successFunction, Consumer<String> errorFunction) {
        if (success) {
            successFunction.accept(message);
        } else {
            errorFunction.accept(message);
        }
    }

    @Override
    public void setPaymentAmount(@NonNull String amount) {
        this.amount = amount;
    }

    @Override
    public String getPaymentAmount() {
        return amount;
    }
}
