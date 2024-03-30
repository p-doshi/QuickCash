package dal.cs.quickcash3.field;

public class FieldValidationException extends Exception{
    private final int errorMessageId;
    private static final long serialVersionUID = 1L;

    public FieldValidationException(int errorMessageId) {
        super();
        this.errorMessageId = errorMessageId;
    }

    public FieldValidationException(int errorMessageId, Throwable cause) {
        super(cause);
        this.errorMessageId = errorMessageId;
    }
    public int getErrorMessageId() {
        return errorMessageId;
    }
}
