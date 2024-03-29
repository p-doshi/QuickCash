package dal.cs.quickcash3.field;

public class FieldValidationException extends Exception{
    private final int errorMessageId;

    public FieldValidationException(int errorMessageId) {
        this.errorMessageId = errorMessageId;
    }

    public int getErrorMessageId() {
        return errorMessageId;
    }
}
