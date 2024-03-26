package dal.cs.quickcash3.registration;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

import dal.cs.quickcash3.R;


public class RegistrationPage extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText address;
    private EditText birthYear;
    private EditText birthMonth;
    private EditText birthDay;
    private EditText userName;
    private EditText emailAddress;
    private EditText password;
    private EditText confirmPassword;
    private TextView statusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        init();
    }

    /**
     * Initializes UI components and sets up event listeners. It finds UI elements
     * by their IDs and sets an onClickListener for the confirmation button.
     */
    private void init(){
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        birthYear = findViewById(R.id.birthYear);
        birthMonth = findViewById(R.id.birthMonth);
        birthDay = findViewById(R.id.birthDay);
        userName = findViewById(R.id.userName);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        statusTextView = findViewById(R.id.registrationStatus);
        findViewById(R.id.confirmButton).setOnClickListener(v -> submitForm());
    }

    /**
     * Submits the registration form after validating all input fields. It validates
     * each field using {@code validateField} method and shows an error message
     * if any validation fails.
     */
    private void submitForm(){
        FormValidator formValidator = new FormValidator();

        if (isFieldInvalid(firstName.getText().toString().trim(), formValidator::isFirstNameValid, R.string.invalid_first_name)) return;
        if (isFieldInvalid(lastName.getText().toString().trim(), formValidator::isLastNameValid, R.string.invalid_last_name)) return;
        if (isFieldInvalid(address.getText().toString().trim(), formValidator::isAddressValid, R.string.invalid_address)) return;
        Date birthDate = parseBirthDate();
        if (isFieldInvalid(birthDate, formValidator::isBirthDateValid, R.string.invalid_birth_date)) return;
        if (isFieldInvalid(userName.getText().toString().trim(), formValidator::isUserNameValid, R.string.invalid_user_name)) return;
        if (isFieldInvalid(emailAddress.getText().toString().trim(), formValidator::isEmailValid, R.string.invalid_email_address)) return;
        if (isFieldInvalid(password.getText().toString().trim(), formValidator::isPasswordValid, R.string.invalid_password)) return;
        if (isFieldInvalid(confirmPassword.getText().toString().trim(), text -> formValidator.doPasswordsMatch(password.getText().toString().trim(), text), R.string.passwords_do_not_match)) return;
        // If all inputs are valid
        statusTextView.setText(R.string.registration_successful);
    }

    /**
     * Validates a string field based on the provided validation logic and shows
     * an error message if validation fails.
     *
     * @param fieldValue        The value of the field to be validated.
     * @param validationLogic   The logic used to validate the field value.
     * @param errorMessageResId The resource ID of the error message to be shown if validation fails.
     * @return true if the field value passes the validation logic, false otherwise.
     */
    private boolean isFieldInvalid(String fieldValue, Predicate<String> validationLogic, int errorMessageResId) {
        if (!validationLogic.test(fieldValue)) {
            statusTextView.setText(errorMessageResId);
            return true;
        }
        return false;
    }

    private boolean isFieldInvalid(Date fieldValue, Predicate<Date> validationLogic, int errorMessageResId) {
        if (fieldValue == null || !validationLogic.test(fieldValue)) {
            statusTextView.setText(errorMessageResId);
            return true;
        }
        return false;
    }

    /**
     * Parses the birth date entered by the user from the year, month, and day fields.
     * It combines the separate strings into a date format and attempts to parse it.
     *
     * @return The parsed {@link Date} object representing the user's birth date,
     * or null if parsing fails.
     */
    private Date parseBirthDate() {
        try {
            String birthDateString = birthYear.getText().toString().trim() + "-" +
                    birthMonth.getText().toString().trim() + "-" +
                    birthDay.getText().toString().trim();
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthDateString);
        } catch (ParseException e) {
            Log.w("RegistrationPage", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}