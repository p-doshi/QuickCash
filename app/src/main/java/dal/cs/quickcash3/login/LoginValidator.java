package dal.cs.quickcash3.login;

import androidx.annotation.NonNull;

public final class LoginValidator {
    // Utility class cannot be instantiated.
    private LoginValidator() {}

    public static boolean isValidEmail(@NonNull String email){
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isEmptyEmail (@NonNull String email){
        return email.isEmpty();
    }

    public static boolean isEmptyPassword (@NonNull String password){
        return password.isEmpty();
    }
}
