package com.example.csci3130_group_3.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

}
