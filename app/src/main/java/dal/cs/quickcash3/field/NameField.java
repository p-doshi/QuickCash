package dal.cs.quickcash3.field;

import android.widget.EditText;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.registration.FieldValidationException;

public class NameField implements FormField {
    private final EditText firstNameEditText;
    private final EditText lastNameEditText;

    public NameField(EditText firstNameEditText,EditText lastNameEditText) {
        this.firstNameEditText = firstNameEditText;
        this.lastNameEditText = lastNameEditText;
    }
    @Override
    public void validate() throws FieldValidationException {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        if (firstName.isEmpty()) {
            throw new FieldValidationException(R.string.invalid_first_name);
        }
        if (lastName.isEmpty()) {
            throw new FieldValidationException(R.string.invalid_last_name);
        }
    }
}
