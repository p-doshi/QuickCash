package dal.cs.quickcash3.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import dal.cs.quickcash3.login.SignInInterface;

public class GoogleSignInHelper {
    private final GoogleSignInClient mGoogleSignInClient;
    private final SignInInterface mSignInInterface;

    public GoogleSignInHelper(Context context, SignInInterface signInInterface) {
        mSignInInterface = signInInterface;

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

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                mSignInInterface.moveToDashboard();
            }
        } catch (ApiException e) {
            // Handle sign-in failure (e.g., display Toast)
            Log.w("GoogleSignInHelper", "signInResult:failed code=" + e.getStatusCode());
            mSignInInterface.setStatusMessage("Google Sign-In failed");
        }
    }
}
