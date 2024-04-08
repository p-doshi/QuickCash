package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.function.Consumer;

public class GeneralField implements FormField {
    private final EditText editText;
    private final int errorMessageId;
    private final Consumer<String> fieldReceiver;

    public GeneralField(@NonNull EditText editText, @StringRes int errorMessageId, @NonNull Consumer<String> fieldReceiver) {
        this.editText = editText;
        this.errorMessageId = errorMessageId;
        this.fieldReceiver = fieldReceiver;
    }


    @Override
    public void retrieve() throws FieldValidationException {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            throw new FieldValidationException(errorMessageId);
        }
        fieldReceiver.accept(text);
    }
}
