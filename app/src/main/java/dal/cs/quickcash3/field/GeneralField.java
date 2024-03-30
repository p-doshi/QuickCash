package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;

public class GeneralField implements FormField {
    private final EditText editText;
    private final int errorMessageId;

    public GeneralField(@NonNull EditText editText, int errorMessageId) {
        this.editText = editText;
        this.errorMessageId = errorMessageId;
    }


    @Override
    public void validate() throws FieldValidationException {
        String userName = editText.getText().toString().trim();
        if (userName.isEmpty()) {
            throw new FieldValidationException(errorMessageId);
        }
    }
}
