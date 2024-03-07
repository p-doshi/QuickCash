package com.example.csci3130_group_3;


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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity  {


    private FirebaseAuth mAuth;
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

        setContentView(R.layout.activity_login);

        this.setUpLoginButton();
        this.setUpSignUpButtonManual();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.WebClient))
                .requestEmail()
                .build();
        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Handle the sign-in success
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                });
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.signupGoogle);
        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }


    protected void checkUserinDatabase(String email, String password){

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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
    protected void moveToDashboard() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("nP5exoTNYnlqpPD1B3BHeuNDcWaPxI").child("test").child("users");;

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);


                        if (email != null) {
                            assert user != null;
                            if (email.equals(user.getEmail())) {
                                Intent dashboardIntent = null;
                                if (role != null) {
                                    if (role.equals(getResources().getString(R.string.employer))) {
                                        dashboardIntent = new Intent(getBaseContext(), EmployerDashboard.class);
                                    } else if (role.equals(getResources().getString(R.string.worker))) {
                                        dashboardIntent = new Intent(getBaseContext(), WorkerDashboard.class);
                                    } else {
                                        Log.wtf(getResources().getString(R.string.choose_role), getResources().getString(R.string.error_choose_role));
                                        System.exit(1);
                                    }
                                    startActivity(dashboardIntent);
                                } else {
                                    // Handle if role is not found for the user
                                    Log.e("RoleError", "User role not found in database");
                                }
                                return; // Exit the loop once the user role is found
                            }
                        }
                    }
                    // Handle if user data not found
                    Log.e("DataError", "User data not found in database");
                } else {
                    // Handle if users node not found
                    Log.e("DataError", "Users node not found in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Throwable databaseError = null;
                Log.e("DataError", Objects.requireNonNull(databaseError.getMessage()));
            }

        });

    }

    protected void moveToRegistration(){
        //Toast.makeText(this, getResources().getString(R.string.SIGNUP_TOAST), Toast.LENGTH_SHORT).show();
        Intent registrationIntent = new Intent(getBaseContext(), RegistrationPage.class);
        startActivity(registrationIntent);
    }

    protected void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }




    public void handleLoginButtonClick(){
        String emailAddress = getEmailAddress();
        String password = getPassword();
        String errorMessage;

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