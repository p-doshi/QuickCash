package dal.cs.quickcash3.login;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.registration.RegistrationPage;

public class LoginActivity extends AppCompatActivity implements SignInInterface {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private Database database;
    private FirebaseAuth mAuth;
    private GoogleSignInHelper mGoogleSignInHelper;
    private SharedPreferences.Editor editor;
    private ActivityResultLauncher<Intent> signInLauncher;
    private DashboardLauncher launcher;
    private TextView statusLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initInterfaces();
        launcher = new DashboardLauncher(this, database);

        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        this.editor = preferences.edit();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && preferences.getBoolean("remember", false)) {
           launchDashboard(user);
           return;
        }
        else {
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

    private void signIn(@NonNull String email, @NonNull String password) {
        CheckBox rememberMe = findViewById(R.id.checkBox);
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(task -> {
                // Sign in success, update UI with the signed-in user's information
                editor.putBoolean("remember", rememberMe.isChecked());
                editor.apply();

                FirebaseUser user = task.getUser();
                if (user == null) {
                    statusLabel.setText(getString(R.string.INVALID_CREDENTIALS));
                }
                else {
                    Log.i(LOG_TAG, "Sign In Success");
                    launchDashboard(user);
                }
            })
            .addOnFailureListener(task -> {
                // If sign in fails, display a message to the user.
                Log.w(LOG_TAG, "Sign In failure: ", task.getCause());
                statusLabel.setText(getString(R.string.INVALID_CREDENTIALS));
            });
    }


    private @NonNull String getEmailAddress(){
        EditText emailInput = findViewById(R.id.emailaddress);
        return emailInput.getText().toString().trim();
    }

    private @NonNull String getPassword(){
        EditText passwordInput = findViewById(R.id.etPassword);
        return passwordInput.getText().toString().trim();
    }

    private void launchDashboard(@NonNull FirebaseUser user) {
        String uid = user.getUid();
        launcher.launchDashboard(uid);
    }

    private void moveToRegistration(){
        Intent registrationIntent = new Intent(getBaseContext(), RegistrationPage.class);
        startActivity(registrationIntent);

        // Stop this activity.
        finish();
    }

    public void handleLoginButton() {
        String emailAddress = getEmailAddress();
        String password = getPassword();

        if (LoginValidator.isEmptyEmail(emailAddress)) {
            statusLabel.setText(R.string.EMPTY_EMAIL_TOAST);
        }
        else if (LoginValidator.isEmptyPassword(password)) {
            statusLabel.setText(R.string.EMPTY_PASSWORD_TOAST);
        }
        else if (!LoginValidator.isValidEmail(emailAddress)) {
            statusLabel.setText(R.string.INVALID_EMAIL_TOAST);
        }
        else {
            signIn(emailAddress, password);
        }
    }

    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.i(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }
    }

    public @NonNull Database getDatabase() {
        return database;
    }
}