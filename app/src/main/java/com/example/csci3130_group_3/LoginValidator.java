package com.example.csci3130_group_3;

public class LoginValidator {

    public static boolean isValidEmail(String email){

        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
