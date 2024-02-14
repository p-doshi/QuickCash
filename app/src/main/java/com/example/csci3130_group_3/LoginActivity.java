package com.example.csci3130_group_3;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Database db;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.clickLoginButton();
        this.clickSignUpButtonManual();
    }
    protected void userValidator(){
        db = new MyFirebaseDatabase(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            mAuth.signInWithEmailAndPassword(getEmailAddress(), getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCustomToken:success");
                                user = mAuth.getCurrentUser();
                                moveToDashboard();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.INVALID_CREDENTIALS), Toast.LENGTH_SHORT);
                                toast.show();
                                setStatusMessage(getResources().getString(R.string.INVALID_CREDENTIALS));

                            }
                        }
                    });
        }
        else {
            moveToDashboard();
        }
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
    }protected void clickSignUpButtonManual(){
        Button signupButton = findViewById(R.id.signupManually);
        signupButton.setOnClickListener(this);
    }
    protected void moveToDashboard(){
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.VALID_TOAST), Toast.LENGTH_SHORT);
        toast.show();
    }
    protected void moveToRegistration(){
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.SIGNUP_TOAST), Toast.LENGTH_SHORT);
        toast.show();
    }
    protected void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.continueButton){
            handleLoginButtonClick();}
        else if (v.getId()==R.id.signupManually) {
            moveToRegistration();
        }
    }

    public void handleLoginButtonClick(){
        String emailAddress = getEmailAddress();
        String password = getPassword();
        String errorMessage = new String();

        if (LoginValidator.isEmptyEmail(emailAddress)) {
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_TOAST);
        }
        if (LoginValidator.isEmptyPassword(password)) {
            errorMessage = getResources().getString(R.string.EMPTY_PASSWORD_TOAST);
        }
        if (!(LoginValidator.isValidEmail(emailAddress)) && !(LoginValidator.isEmptyEmail(emailAddress))) {
            errorMessage = getResources().getString(R.string.INVALID_EMAIL_TOAST);
        }
        if (LoginValidator.isValidEmail(emailAddress) && !(LoginValidator.isEmptyPassword(password))) {
            userValidator();
            return;
        }
        setStatusMessage(errorMessage);
    }


}