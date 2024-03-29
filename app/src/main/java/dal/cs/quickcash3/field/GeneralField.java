package dal.cs.quickcash3.field;

import android.widget.EditText;

public class GeneralField implements FormField {
    private final EditText editText;
    private final int errorMessageId;

    public GeneralField(EditText editText, int errorMessageId) {
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
