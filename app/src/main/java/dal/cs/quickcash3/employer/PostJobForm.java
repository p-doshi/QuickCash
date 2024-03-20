package dal.cs.quickcash3.employer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseExampleActivity;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;

/**
 * @author Hayely Vezeau
 * Initialize UI for Post job form
 */
public class PostJobForm extends Activity {
    Database database;
    private static final String LOG_TAG = PostJobForm.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_job);
        initInterfaces();

        // initialize spinners
        this.setUpDurationSpinner();
        this.setUpUrgencySpinner();
        this.setUpProvinceSpinner();

        this.setUpConfirmPostButton();
    }

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

    public @NonNull Database getDatabase() {
        return database;
    }

    /**
     * handle on click of confirmation button
     */
    protected void setUpConfirmPostButton(){
        Button confirmPostButton = findViewById(R.id.addJobConfirmButton);
        confirmPostButton.setOnClickListener(view -> {
            // check fields
            String errorMessage = checkAllFields();
            TextView status = findViewById(R.id.jobSubmitStatus);

            if(!"".equals(errorMessage)){
                // handle error message
                status.setText(errorMessage);
            }
            else{
                // save to db
                createJob();
                // write success message
                status.setText(R.string.success);
                // move to next page
            }

        });
    }

    /**
     * Initiate duration spinner
     */
    protected void setUpDurationSpinner(){
        Spinner durationSpinner = findViewById(R.id.jobDurationSpinner);

        String[] durationValues = getResources().getStringArray(R.array.durationSpinnerValues);

        ArrayAdapter<String> durationAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durationValues);
        durationAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAA);
    }

    /**
     * Initiate urgency spinner
     */
    protected void setUpUrgencySpinner(){
        Spinner urgencySpinner = findViewById(R.id.jobUrgencySpinner);

        String[] urgencyValues = getResources().getStringArray(R.array.urgencySpinnerValues);

        ArrayAdapter<String> urgencyAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urgencyValues);
        urgencyAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgencySpinner.setAdapter(urgencyAA);
    }

    /**
     * Initiate province spinner
     */
    protected void setUpProvinceSpinner(){
        Spinner provinceSpinner = findViewById(R.id.addJobProvince);

        String[] provinceValues = getResources().getStringArray(R.array.provinceSpinnerValues);

        ArrayAdapter<String> provinceAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceValues);
        provinceAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAA);
    }

    /**
     * Method to check that all input values are valid
     * @return A string holding an error message; empty when no input errors
     */
    protected @NonNull String checkAllFields(){
        // check fields and return error message?
        Map<String, String> fields = getFieldsMap();
        return PostJobFormFields.checkFieldsValid(fields);
    }

    /**
     * Creates a new available job in the database
     */
    protected void createJob() {
        Map<String, String> fields = getFieldsMap();
        AvailableJob job = PostAvailableJobHelper.createAvailableJob(fields, this);
        job.writeToDatabase(database, error-> Log.e("PostJobForm", error));
    }

    // Getters
    protected @NonNull String getJobTitle(){
        EditText jobTitle = findViewById(R.id.jobPostingTitle);
        return jobTitle.getText().toString().trim();
    }
    protected @NonNull String getJobDate(){
        EditText jobDate = findViewById(R.id.addJobDate);
        return jobDate.getText().toString().trim();
    }
    protected @NonNull String getJobSalary(){
        EditText jobSalary = findViewById(R.id.addJobSalary);
        return jobSalary.getText().toString().trim();
    }
    protected @NonNull String getJobAddress(){
        EditText jobAddress = findViewById(R.id.addJobAddress);
        return jobAddress.getText().toString().trim();
    }
    protected @NonNull String getJobCity(){
        EditText jobCity = findViewById(R.id.addJobCity);
        return jobCity.getText().toString().trim();
    }
    protected @NonNull String getJobDescription(){
        EditText jobDescription = findViewById(R.id.addJobDescription);
        return jobDescription.getText().toString().trim();
    }
    protected @NonNull String getDuration() {
        Spinner jobDuration = findViewById(R.id.jobDurationSpinner);
        return jobDuration.getSelectedItem().toString();
    }
    protected @NonNull String getUrgency() {
        Spinner jobUrgency = findViewById(R.id.jobUrgencySpinner);
        return jobUrgency.getSelectedItem().toString();
    }
    protected @NonNull String getProvince() {
        Spinner jobProvince = findViewById(R.id.addJobProvince);
        return jobProvince.getSelectedItem().toString();
    }

    /**
     * Method to create a hashmap with all the fields as the keys and the user input as values
     * @return a hashmap
     */
    protected @NonNull Map<String, String> getFieldsMap(){
        Map<String, String> fields = new HashMap<>();

        fields.put("title", getJobTitle());
        fields.put("date", getJobDate());
        fields.put("salary", getJobSalary());
        fields.put("address", getJobAddress());
        fields.put("city", getJobCity());
        fields.put("province", getProvince());
        fields.put("duration", getDuration());
        fields.put("urgency", getUrgency());
        fields.put("description", getJobDescription());
        return fields;
    }
}
