package dal.cs.quickcash3.employer;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;

import dal.cs.quickcash3.employer.PostJobForm;


public class EmployerDashboard extends FragmentActivity {
    Database database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);
        this.setUpAddJobButton();
        initInterfaces();
    }


    @SuppressLint("RestrictedApi")
    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.d(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }
    }

    public @NonNull Database getDatabase(){
        return database;
    }

    /**
     * handle on click of 'add job' button
     */
    protected void setUpAddJobButton(){
        Button addJobButton = findViewById(R.id.addJobButton);
        addJobButton.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.employerDashFragment, new PostJobForm(database)).commit();

            //move to page
        });
    }

    /*
    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id., fragment);
        transaction.commit();
    }

     */

}