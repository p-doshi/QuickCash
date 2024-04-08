package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;

public class EmailAddressField implements FormField {
    private final EditText editText;
    private final Consumer<String> fieldReceiver;


    public EmailAddressField(@NonNull EditText editText,@NonNull Consumer<String> fieldReceiver) {
        this.editText = editText;
        this.fieldReceiver = fieldReceiver;
    }

    @Override
    public void retrieve() throws FieldValidationException {
        String text = editText.getText().toString().trim();
        String addressPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        if(!text.matches(addressPattern)){
            throw new FieldValidationException(R.string.invalid_email_address);
        }
        fieldReceiver.accept(text);
    }
}
