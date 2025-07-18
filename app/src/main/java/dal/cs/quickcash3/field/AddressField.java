package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;

import dal.cs.quickcash3.R;

public class AddressField implements FormField {
    final private EditText editText;

    public AddressField(@NonNull EditText editText) {
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
