package dal.cs.quickcash3.registration;

public class FieldValidationException extends Exception{
    private final int errorMessageId;

    public FieldValidationException(int errorMessageId) {
        this.errorMessageId = errorMessageId;
    }

    public int getErrorMessageId() {
        return errorMessageId;
    }
}
