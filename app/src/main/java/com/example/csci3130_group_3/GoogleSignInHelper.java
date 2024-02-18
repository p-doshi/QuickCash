package com.example.csci3130_group_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleSignInHelper {

    private final GoogleSignInClient mGoogleSignInClient;
    private final FirebaseAuth mAuth;
    private final SharedPreferences.Editor editor;

    public GoogleSignInHelper(AppCompatActivity activity, ActivityResultLauncher<Intent> signInLauncher) {
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.WebClient))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences preferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        this.editor = preferences.edit();

        SignInButton signInButton = activity.findViewById(R.id.signupGoogle);
        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask, AppCompatActivity activity) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                ((LoginActivity) activity).moveToDashboard(); // Call moveToDashboard() method from LoginActivity
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login_Tag", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(activity, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }
    public void checkUserInDatabase(String email, String password, CheckBox rememberMe, AppCompatActivity activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(activity, task -> {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login_Tag", "signInWithEmailAndPassword:success");
                    editor.putBoolean("remember", rememberMe.isChecked());
                    editor.apply();
                    ((LoginActivity) activity).moveToDashboard();
                })
                .addOnFailureListener(activity, task -> {
                    // If sign in fails, display a message to the user.
                    Log.w("Login_Tag", "signInWithEmailAndPassword:failure", task.getCause());
                    ((LoginActivity) activity).setStatusMessage(activity.getResources().getString(R.string.INVALID_CREDENTIALS));
                });
    }
}
