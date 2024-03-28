package dal.cs.quickcash3.registration;

import android.widget.EditText;

import dal.cs.quickcash3.R;

public class AddressField implements RegistrationFormField {
    final private EditText editText;

    public AddressField(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void isValid() throws FieldValidationException {
        String text = editText.getText().toString().trim();
        String addressPattern = "\\d+\\s+\\w+\\s+\\w+";
        if(!text.matches(addressPattern)){
            throw new FieldValidationException(R.string.invalid_address);
        }
    }
}
