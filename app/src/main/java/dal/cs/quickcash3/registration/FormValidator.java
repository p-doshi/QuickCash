package dal.cs.quickcash3.registration;

import static dal.cs.quickcash3.util.StringHelper.isBlank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.regex.Pattern;

public class FormValidator {
    // Validation methods
    public boolean isFirstNameValid(@NonNull String firstName) {
        return isBlank(firstName);
    }

    public boolean isLastNameValid(@NonNull String lastName) {
        return isBlank(lastName);
    }

    public boolean isAddressValid(@NonNull String address) {
        return isBlank(address);
    }

    public boolean isBirthDateValid(@Nullable Date birthDate) {
        return birthDate != null;
    }

    public boolean isUserNameValid(@NonNull String userName) {
        return isBlank(userName);
    }

    public boolean isEmailValid(@NonNull String email) {
        return Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email);
    }

    public boolean isPasswordValid(@NonNull String password) {
        @NonNull String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    public boolean doPasswordsMatch(@NonNull String password, @NonNull String confirmPassword) {
        return password.equals(confirmPassword);
    }

}
