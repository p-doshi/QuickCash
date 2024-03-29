package dal.cs.quickcash3.field;

import android.widget.EditText;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.registration.FieldValidationException;

public class GeneralField implements FormField {
    private final EditText editText;

    public GeneralField(EditText editText) {
        this.editText = editText;
    }


    @Override
    public void validate() throws FieldValidationException {
        String userName = editText.getText().toString().trim();
        if (userName.isEmpty()) {
            throw new FieldValidationException(R.string.invalid_user_name);
        }
    }
}
