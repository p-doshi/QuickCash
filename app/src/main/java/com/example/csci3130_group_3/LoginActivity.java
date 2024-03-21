package com.example.csci3130_group_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dal.cs.quickcash3.R;

public class LoginActivity extends AppCompatActivity implements SignInInterface {

    private FirebaseAuth mAuth;
    private GoogleSignInHelper mGoogleSignInHelper;
    private SharedPreferences.Editor editor;
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        this.editor = preferences.edit();
        if(mAuth.getCurrentUser()!=null && preferences.getBoolean("remember",false)){
           moveToDashboard();
        }else{
            editor.putBoolean("remember",false);
            editor.apply();
            mAuth.signOut();
        }

        setContentView(R.layout.activity_login);

        this.setUpLoginButton();
        this.setUpSignUpButtonManual();

        mGoogleSignInHelper = new GoogleSignInHelper(this,  new SignInImplementation(this));
        SignInButton signInButton = findViewById(R.id.signupGoogle);
        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = mGoogleSignInHelper.getSignedInAccountFromIntent(data);
                        mGoogleSignInHelper.handleSignInResult(task);
                    } else {
                        Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                    }
                });

        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInHelper.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }
    
    protected void checkUserInDatabase(String email, String password){

        CheckBox rememberMe = findViewById(R.id.checkBox);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, task -> {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login_Tag", "signInWithCustomToken:success");
                        editor.putBoolean("remember", rememberMe.isChecked());
                        editor.apply();
                        moveToDashboard();
                })
                .addOnFailureListener(this, task -> {
                        // If sign in fails, display a message to the user.
                        Log.w("Login_Tag", "signInWithCustomToken:failure", task.getCause());
                        setStatusMessage(getResources().getString(R.string.INVALID_CREDENTIALS));
                });

    }

    protected String getEmailAddress(){
        EditText emailInput = findViewById(R.id.emailAddress);
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

    protected void setUpSignUpButtonManual(){
        Button signupButton = findViewById(R.id.signupManually);
        signupButton.setOnClickListener(view -> moveToRegistration());
    }

    @Override
    public void moveToDashboard(){
        setStatusMessage("");
        Toast.makeText(this, getResources().getString(R.string.VALID_TOAST), Toast.LENGTH_SHORT).show();
    }

    protected void moveToRegistration(){
        Toast.makeText(this, getResources().getString(R.string.SIGNUP_TOAST), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message);
    }

    public void handleLoginButtonClick(){
        String emailAddress = getEmailAddress();
        String password = getPassword();
        String errorMessage;

        if (LoginValidator.isEmptyEmail(emailAddress)) {
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_TOAST);
            setStatusMessage(errorMessage);
        }else if (LoginValidator.isEmptyPassword(password)) {
            errorMessage = getResources().getString(R.string.EMPTY_PASSWORD_TOAST);
            setStatusMessage(errorMessage);
        }else if (!(LoginValidator.isValidEmail(emailAddress))) {
            errorMessage = getResources().getString(R.string.INVALID_EMAIL_TOAST);
            setStatusMessage(errorMessage);
        }else {
            checkUserInDatabase(emailAddress,password);
        }
    }
}