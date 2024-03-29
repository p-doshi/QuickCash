package dal.cs.quickcash3.field;

import android.widget.EditText;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.registration.FieldValidationException;

public class AddressField implements FormField {
    final private EditText editText;

    public AddressField(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void validate() throws FieldValidationException {
        String text = editText.getText().toString().trim();
        String addressPattern = "\\d+\\s+\\w+\\s+\\w+";
        if(!text.matches(addressPattern)){
            throw new FieldValidationException(R.string.invalid_address);
        }
    }
}
