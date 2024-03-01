package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationPage extends AppCompatActivity {

    private EditText firstName, lastName, address, birthYear, birthMonth, birthDay, userName, emailAddress, password, confirmPassword;
    TextView statusTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        init();
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
        findViewById(R.id.confirmButton).setOnClickListener(v -> submitForm());
        statusTextView = findViewById(R.id.status);
    }

    private void submitForm(){
        FormValidator formValidator = new FormValidator();

        String firstNameText = firstName.getText().toString().trim();
        String lastNameText = lastName.getText().toString().trim();
        String addressText = address.getText().toString().trim();
        String userNameText = userName.getText().toString().trim();
        String emailText = emailAddress.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        Date birthDate = null;
        try {
            String birthDateString = birthYear.getText().toString() + "-" + birthMonth.getText().toString() + "-" + birthDay.getText().toString();
            birthDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!formValidator.isFirstNameValid(firstNameText)) {
            statusTextView.setText(R.string.invalid_first_name);
        } else if (!formValidator.isLastNameValid(lastNameText)) {
            statusTextView.setText(R.string.invalid_last_name);
        } else if (!formValidator.isAddressValid(addressText)) {
            statusTextView.setText(R.string.invalid_address);
        } else if (!formValidator.isBirthDateValid(birthDate)) {
            statusTextView.setText(R.string.invalid_birth_date);
        } else if (!formValidator.isUserNameValid(userNameText)) {
            statusTextView.setText(R.string.invalid_user_name);
        } else if (!formValidator.isEmailValid(emailText)) {
            statusTextView.setText(R.string.invalid_email_address);
        } else if (!formValidator.isPasswordValid(passwordText)) {
            statusTextView.setText(R.string.invalid_password);
        } else if (!formValidator.doPasswordsMatch(passwordText, confirmPasswordText)) {
            statusTextView.setText(R.string.passwords_do_not_match);
        } else {
            // all inputs is valid
            statusTextView.setText(R.string.registration_successful);
            moveToChooseRoleWindow();
        }
    }

    protected void moveToChooseRoleWindow() {
        Intent chooseRoleIntent = new Intent(getBaseContext(), ChooseRoleActivity.class);
        startActivity(chooseRoleIntent);
    }
}