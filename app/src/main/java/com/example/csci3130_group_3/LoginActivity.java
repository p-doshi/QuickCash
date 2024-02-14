package com.example.csci3130_group_3;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences preferences ;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preferences= getSharedPreferences("UserData", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null){
           userValidator("","");
        }
        
        this.clickLoginButton();
        this.clickSignUpButtonManual();
        this.clickSignUpButtonGoogle();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.WebClient))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.signupGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    protected void userValidator(String email, String password){

        CheckBox rememberMe = findViewById(R.id.checkBox);
        this.editor = preferences.edit();

        if (user == null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && rememberMe.isChecked()) { // refactor nested ifs
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(getResources().getString(R.string.LOGINACTIVITY_TAG), "signInWithCustomToken:success");
                                user = mAuth.getCurrentUser();
                                editor.putString(getResources().getString(R.string.EMAIL_KEY), email);
                                editor.putString(getResources().getString(R.string.PASSWORD_KEY), password);
                                editor.apply();
                                moveToDashboard();
                            } else if(task.isSuccessful() && !(rememberMe.isChecked())) {
                                Log.d(getResources().getString(R.string.LOGINACTIVITY_TAG), "signInWithCustomToken:success");
                                user = mAuth.getCurrentUser();
                                moveToDashboard();
                            }else{
                                // If sign in fails, display a message to the user.
                                Log.w(getResources().getString(R.string.LOGINACTIVITY_TAG), "signInWithCustomToken:failure", task.getException());
                                setStatusMessage(getResources().getString(R.string.INVALID_CREDENTIALS));
                            }
                        }
                    });
        }
        else {
            moveToDashboard();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(getResources().getString(R.string.LOGINACTIVITY_TAG), "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            moveToDashboard();
        } else {
            return;
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    protected void clickSignUpButtonManual(){
        Button signupButton = findViewById(R.id.signupManually);
        signupButton.setOnClickListener(this);
    }
    protected void clickSignUpButtonGoogle(){
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
        }else if(v.getId()==R.id.signupGoogle){

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
            userValidator(emailAddress,password);
            return;
        }
        setStatusMessage(errorMessage);
    }


}