package com.example.csci3130_group_3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignInHelper {
    private final GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInHelper(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.WebClient))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public Task<GoogleSignInAccount> getSignedInAccountFromIntent(Intent data) {
        return GoogleSignIn.getSignedInAccountFromIntent(data);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask, LoginActivity activity) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                activity.moveToDashboard();
            }
        } catch (ApiException e) {
            // Handle sign-in failure (e.g., display Toast)
            Log.w("GoogleSignInHelper", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(activity, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }
}
