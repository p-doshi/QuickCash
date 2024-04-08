package dal.cs.quickcash3.field;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class FieldValidationException extends Exception{
    private final int errorMessageId;
    private static final long serialVersionUID = 1L;

    public FieldValidationException(@StringRes int errorMessageId) {
        super();
        this.errorMessageId = errorMessageId;
    }

    public FieldValidationException(@StringRes int errorMessageId,@Nullable Throwable cause) {
        super(cause);
        this.errorMessageId = errorMessageId;
    }
    public @StringRes int getErrorMessageId() {
        return errorMessageId;
    }
}
