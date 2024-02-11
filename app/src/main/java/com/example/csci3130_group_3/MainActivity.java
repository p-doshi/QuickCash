package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailInput;
    TextInputEditText passwordInput;
    Snackbar toastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.clickLoginButton();
    }
    protected String getEmailAddress(){
        EditText emailInput = findViewById(R.id.emailaddress);
        return emailInput.getText().toString().trim();
    }
    protected String getPassword(){
        EditText passwordInput = findViewById(R.id.etPassword);
        return passwordInput.getText().toString().trim();
    }

    protected void clickLoginButton(){
        Button loginButton = findViewById(R.id.continueButton);
        loginButton.setOnClickListener(this);
    }
    protected void moveToDashboard(){

    }
    protected void moveToRegistration(){

    }
    protected void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }
    @Override
    public void onClick(View v) {
        String emailAddress = getEmailAddress();
        String password = getPassword();
        String errorMessage = new String();

        if(LoginValidator.isEmptyEmail(emailAddress)){
            errorMessage=getResources().getString(R.string.EMPTY_EMAIL_TOAST);
        }if(LoginValidator.isEmptyPassword(password)){
            errorMessage=getResources().getString(R.string.EMPTY_PASSWORD_TOAST);
        }
        if(!(LoginValidator.isValidEmail(emailAddress))&&!(LoginValidator.isEmptyEmail(emailAddress))){
        }

        setStatusMessage(errorMessage);
    }
}