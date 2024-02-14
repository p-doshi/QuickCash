package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationPage extends AppCompatActivity {

    private EditText firstName, lastName, address, birthYear, birthMonth, birthDay, userName, emailAddress, password, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        init();
        findViewById(R.id.confirmButton).setOnClickListener(v -> submitForm());
    }

    private void init(){
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        birthYear = findViewById(R.id.birthYear);
        birthMonth = findViewById(R.id.birthMonth);
        birthDay = findViewById(R.id.birthDay);
        userName = findViewById(R.id.userName);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
    }

    private void submitForm(){
        TextView statusTextView = findViewById(R.id.status);
        RegistrationForm formValidator = new RegistrationForm();

        String firstNameText = firstName.getText().toString();
        String lastNameText = lastName.getText().toString();
        String addressText = address.getText().toString();
        String userNameText = userName.getText().toString();
        String emailText = emailAddress.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();

        Date birthDate = null;
        try {
            String birthDateString = birthYear.getText().toString() + "-" + birthMonth.getText().toString() + "-" + birthDay.getText().toString();
            birthDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!formValidator.isFirstNameValid(firstNameText)) {
            statusTextView.setText("Invalid first name");
        } else if (!formValidator.isLastNameValid(lastNameText)) {
            statusTextView.setText("Invalid last name");
        } else if (!formValidator.isAddressValid(addressText)) {
            statusTextView.setText("Invalid address");
        } else if (!formValidator.isBirthDateValid(birthDate)) {
            statusTextView.setText("Invalid birth date");
        } else if (!formValidator.isUserNameValid(userNameText)) {
            statusTextView.setText("Invalid user name");
        } else if (!formValidator.isEmailValid(emailText)) {
            statusTextView.setText("Invalid email address");
        } else if (!formValidator.isPasswordValid(passwordText)) {
            statusTextView.setText("Invalid password");
        } else if (!formValidator.doPasswordsMatch(passwordText, confirmPasswordText)) {
            statusTextView.setText("Passwords do not match");
        } else {
            // all inputs is valid
            statusTextView.setText("Registration successful!");
        }
    }
}