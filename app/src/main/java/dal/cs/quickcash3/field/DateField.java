package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.function.Consumer;

import dal.cs.quickcash3.R;

public class DateField implements FormField {
    private final EditText yearEditText;
    private final EditText monthEditText;
    private final EditText dayEditText;
    private final Consumer<LocalDate> fieldReceiver;

    public DateField(@NonNull EditText yearEditText,@NonNull EditText monthEditText,@NonNull EditText dayEditText,@NonNull Consumer<LocalDate> fieldReceiver) {
        this.yearEditText = yearEditText;
        this.monthEditText = monthEditText;
        this.dayEditText = dayEditText;
        this.fieldReceiver = fieldReceiver;
    }


    @Override
    public void retrieve() throws FieldValidationException {
        try {
            int year = Integer.parseInt(yearEditText.getText().toString());
            int month = Integer.parseInt(monthEditText.getText().toString());
            int day = Integer.parseInt(dayEditText.getText().toString());
            LocalDate date = LocalDate.of(year, month, day);
            fieldReceiver.accept(date);
        } catch (DateTimeException | NumberFormatException e) {
            throw new FieldValidationException(R.string.invalid_birth_date, e);
        }
    }

}
