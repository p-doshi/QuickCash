package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;

public class AddressField implements FormField {
    private final EditText editText;
    private final Consumer<String> fieldReceiver;

    public AddressField(@NonNull EditText editText,@NonNull Consumer<String> fieldReceiver) {
        this.editText = editText;
        this.fieldReceiver = fieldReceiver;
    }

    @Override
    public void retrieve() throws FieldValidationException {
        String text = editText.getText().toString().trim();
        String addressPattern = "\\d+\\s+\\w+\\s+\\w+";
        if(!text.matches(addressPattern)){
            throw new FieldValidationException(R.string.invalid_address);
        }
        fieldReceiver.accept(text);
    }
}
