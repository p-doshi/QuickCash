package dal.cs.quickcash3.registration;
import java.util.Date;
import java.util.regex.Pattern;
public class FormValidator {


    // Validation methods
    public boolean isFirstNameValid(String firstName) {
        return firstName != null && !firstName.trim().isEmpty();
    }

    public boolean isLastNameValid(String lastName) {
        return lastName != null && !lastName.trim().isEmpty();
    }

    public boolean isAddressValid(String address) {
        return address != null && !address.trim().isEmpty();
    }

    public boolean isBirthDateValid(Date birthDate) {
        return birthDate != null;
    }

    public boolean isUserNameValid(String userName) {
        return userName != null && !userName.trim().isEmpty();
    }

    public boolean isEmailValid(String email) {
        return email != null && Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email);
    }

    public boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password != null && Pattern.matches(passwordPattern, password);
    }

    public boolean doPasswordsMatch(String password,String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

}
