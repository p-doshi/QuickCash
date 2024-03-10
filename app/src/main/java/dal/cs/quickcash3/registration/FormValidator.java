package dal.cs.quickcash3.registration;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.Date;
import java.util.regex.Pattern;

public class FormValidator {
    private static boolean containsCharacter(@NonNull String str) {
        boolean foundCharacter = false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                foundCharacter = true;
            }
        }
        return foundCharacter;
    }

    public boolean isFirstNameValid(@NonNull String firstName) {
        return containsCharacter(firstName);
    }

    public boolean isLastNameValid(@NonNull String lastName) {
        return containsCharacter(lastName);
    }

    public boolean isAddressValid(@NonNull String address) {
        return containsCharacter(address);
    }

    public boolean isBirthDateValid(@Nullable Date birthDate) {
        return birthDate != null;
    }

    public boolean isUserNameValid(@NonNull String userName) {
        return containsCharacter(userName);
    }

    public boolean isEmailValid(@NonNull String email) {
        return Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email);
    }

    public boolean isPasswordValid(@NonNull String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    public boolean doPasswordsMatch(@NonNull String password, @NonNull String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
