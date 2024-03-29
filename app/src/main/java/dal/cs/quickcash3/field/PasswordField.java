package dal.cs.quickcash3.field;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.registration.FieldValidationException;

public class PasswordField implements FormField {
    private final EditText passwordEditText;
    private final EditText confirmPasswordEditText;
    public PasswordField(EditText passwordEditText, EditText confirmPasswordEditText) {
        this.passwordEditText = passwordEditText;
        this.confirmPasswordEditText = confirmPasswordEditText;
    }

    @Override
    public void validate() throws FieldValidationException {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!isPasswordValid(password)) {
            throw new FieldValidationException(R.string.invalid_password);
        }

        if (!doPasswordsMatch(password, confirmPassword)) {
            throw new FieldValidationException(R.string.passwords_do_not_match);
        }
    }
    private boolean isPasswordValid(@NonNull String password) {
        @NonNull String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean doPasswordsMatch(@NonNull String password, @NonNull String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
