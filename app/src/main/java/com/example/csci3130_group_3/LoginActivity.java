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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences.Editor editor;

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

        setContentView(R.layout.activity_main);

        this.setUpLoginButton();
        this.setUpSignUpButtonManual();
        this.setUpSignUpButtonGoogle();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.WebClient))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.signupGoogle);
        signInButton.setOnClickListener((view) -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }


    protected void checkUserinDatabase(String email, String password){

        CheckBox rememberMe = findViewById(R.id.checkBox);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login_Tag", "signInWithCustomToken:success");
                        editor.putBoolean("remember", rememberMe.isChecked());
                        editor.apply();
                        moveToDashboard();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Login_Tag", "signInWithCustomToken:failure", task.getException());
                        setStatusMessage(getResources().getString(R.string.INVALID_CREDENTIALS));
                    }
                });
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
            if (account != null) {
                moveToDashboard();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login_Tag", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
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

    protected void setUpLoginButton(){
        Button loginButton = findViewById(R.id.continueButton);
        loginButton.setOnClickListener(view -> handleLoginButtonClick());
    }

    protected void setUpSignUpButtonManual(){
        Button signupButton = findViewById(R.id.signupManually);
        signupButton.setOnClickListener(view -> moveToRegistration());
    }

    protected void setUpSignUpButtonGoogle(){
        Button signupButton = findViewById(R.id.signupManually);
        signupButton.setOnClickListener(this);
    }

    protected void moveToDashboard(){
        Toast.makeText(this, getResources().getString(R.string.VALID_TOAST), Toast.LENGTH_SHORT).show();
    }

    protected void moveToRegistration(){
        Toast.makeText(this, getResources().getString(R.string.SIGNUP_TOAST), Toast.LENGTH_SHORT).show();
    }

    protected void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }


    @Override
    public void onClick(View v) {
    }


    public void handleLoginButtonClick(){
        String emailAddress = getEmailAddress();
        String password = getPassword();
        String errorMessage = null;

        if (LoginValidator.isEmptyEmail(emailAddress)) {
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_TOAST);
        }else if (LoginValidator.isEmptyPassword(password)) {
            errorMessage = getResources().getString(R.string.EMPTY_PASSWORD_TOAST);
        }else if (!(LoginValidator.isValidEmail(emailAddress))) {
            errorMessage = getResources().getString(R.string.INVALID_EMAIL_TOAST);
        }else {
            checkUserinDatabase(emailAddress,password);
            return;
        }
        setStatusMessage(errorMessage);
    }
}