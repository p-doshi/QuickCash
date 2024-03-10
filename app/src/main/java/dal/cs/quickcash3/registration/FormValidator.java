package dal.cs.quickcash3.registration;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.Date;
import java.util.regex.Pattern;

public class FormValidator {
    private static boolean onlyContainsWhitespace(@NonNull String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isFirstNameValid(@NonNull String firstName) {
        return onlyContainsWhitespace(firstName);
    }

    public boolean isLastNameValid(@NonNull String lastName) {
        return onlyContainsWhitespace(lastName);
    }

    public boolean isAddressValid(@NonNull String address) {
        return onlyContainsWhitespace(address);
    }

    public boolean isBirthDateValid(@Nullable Date birthDate) {
        return birthDate != null;
    }

    public boolean isUserNameValid(@NonNull String userName) {
        return onlyContainsWhitespace(userName);
    }

    public boolean isEmailValid(@NonNull String email) {
        return Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email);
    }

    public boolean isPasswordValid(@NonNull String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    public boolean doPasswordsMatch(@NonNull String password,String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
