package com.example.csci3130_group_3;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  {


    private final GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInHelper googleSignInHelper;

    public LoginActivity(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences.Editor editor = preferences.edit();

        if(mAuth.getCurrentUser()!=null && preferences.getBoolean("remember",false)){
           moveToDashboard();
        }else{
            editor.putBoolean("remember",false);
            editor.apply();
            mAuth.signOut();
        }

        setContentView(R.layout.activity_login);

        this.setUpLoginButton();
        this.setUpSignUpButton();

        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Handle the sign-in success
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    googleSignInHelper.handleSignInResult(task, this);
                });

        googleSignInHelper = new GoogleSignInHelper(this, signInLauncher);
        SignInButton signInButton = findViewById(R.id.signupGoogle);
        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }

    protected String getEmailAddress(){
        EditText emailInput = findViewById(R.id.emailaddress);
        return emailInput.getText().toString().trim();
    }

    protected String getPassword(){
        EditText passwordInput = findViewById(R.id.etPassword);
        return passwordInput.getText().toString().trim();
    }

    protected void setUpLoginButton(){
        Button loginButton = findViewById(R.id.continueButton);
        loginButton.setOnClickListener(view -> handleLoginButtonClick());
    }

    protected void setUpSignUpButton(){
        Button signupButton = findViewById(R.id.signUp);
        signupButton.setOnClickListener(view -> moveToRegistration());
    }

    public void moveToDashboard(){ Toast.makeText(this, getResources().getString(R.string.VALID_TOAST), Toast.LENGTH_SHORT).show();}

    protected void moveToRegistration(){Toast.makeText(this, getResources().getString(R.string.SIGNUP_TOAST), Toast.LENGTH_SHORT).show();}

    protected void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }

    public void handleLoginButtonClick(){
        String emailAddress = getEmailAddress();
        String password = getPassword();
        String errorMessage;
        CheckBox rememberMe = findViewById(R.id.checkBox);

        if (LoginValidator.isEmptyEmail(emailAddress)) {
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_TOAST);
        }else if (LoginValidator.isEmptyPassword(password)) {
            errorMessage = getResources().getString(R.string.EMPTY_PASSWORD_TOAST);
        }else if (!(LoginValidator.isValidEmail(emailAddress))) {
            errorMessage = getResources().getString(R.string.INVALID_EMAIL_TOAST);
        }else {
            googleSignInHelper.checkUserInDatabase(emailAddress,password,rememberMe,this);
            return;
        }
        setStatusMessage(errorMessage);
    }
}