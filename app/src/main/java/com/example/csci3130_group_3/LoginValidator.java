package com.example.csci3130_group_3;

public class LoginValidator {

    public static boolean isValidEmail(String email){
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    public static boolean isEmptyEmail (String email){
        return email.isEmpty();
    }
    public static boolean isEmptyPassword (String password){
        return password.isEmpty();
    }
    public static boolean isEmailInDatabase(String email){
        return false;
    }
}
