package dal.cs.quickcash3.field;

import android.widget.EditText;

import dal.cs.quickcash3.R;

public class EmailAddressField implements FormField {
    final private EditText editText;

    public EmailAddressField(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void validate() throws FieldValidationException {
        String text = editText.getText().toString().trim();
        String addressPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        if(!text.matches(addressPattern)){
            throw new FieldValidationException(R.string.invalid_email_address);
        }
    }
}
