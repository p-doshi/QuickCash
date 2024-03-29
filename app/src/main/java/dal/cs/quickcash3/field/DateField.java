package dal.cs.quickcash3.field;

import android.widget.EditText;

import java.time.DateTimeException;
import java.time.LocalDate;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.registration.FieldValidationException;

public class DateField implements FormField {
    private final EditText yearEditText;
    private final EditText monthEditText;
    private final EditText dayEditText;

    public DateField(EditText yearEditText, EditText monthEditText, EditText dayEditText) {
        this.yearEditText = yearEditText;
        this.monthEditText = monthEditText;
        this.dayEditText = dayEditText;
    }


    @Override
    public void validate() throws FieldValidationException {
        try {
            int year = Integer.parseInt(yearEditText.getText().toString());
            int month = Integer.parseInt(monthEditText.getText().toString());
            int day = Integer.parseInt(dayEditText.getText().toString());
            LocalDate.of(year, month, day);
        } catch (DateTimeException | NumberFormatException e) {
            throw new FieldValidationException(R.string.invalid_birth_date);
        }
    }
}
