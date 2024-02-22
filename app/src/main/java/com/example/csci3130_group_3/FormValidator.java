package com.example.csci3130_group_3;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.regex.Pattern;

public class FormValidator {
    // Validation methods
    public boolean isFirstNameValid(@NonNull String firstName) {
        return !firstName.trim().isEmpty();
    }

    public boolean isLastNameValid(@NonNull String lastName) {
        return !lastName.trim().isEmpty();
    }

    public boolean isAddressValid(@NonNull String address) {
        return !address.trim().isEmpty();
    }

    public boolean isBirthDateValid(@Nullable Date birthDate) {
        return birthDate != null;
    }

    public boolean isUserNameValid(@NonNull String userName) {
        return !userName.trim().isEmpty();
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
