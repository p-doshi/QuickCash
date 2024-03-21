package dal.cs.quickcash3.employer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;

/**
 * @author Hayely Vezeau
 * Initialize UI for Post job form
 */
public class PostJobForm extends Fragment {
    Database database;
    private static final String LOG_TAG = PostJobForm.class.getSimpleName();

    public PostJobForm(@NonNull Database database){
        super();
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View addJobView = inflater.inflate(R.layout.add_job, container, false);
        Context context = getContext();
        super.onCreate(savedInstanceState);

        // initialize spinners
        assert context != null;
        this.setUpDurationSpinner(addJobView, context);
        this.setUpUrgencySpinner(addJobView, context);
        this.setUpProvinceSpinner(addJobView, context);

        this.setUpConfirmPostButton(addJobView, context);
        return addJobView;
    }

    public @NonNull Database getDatabase() {
        return database;
    }

    /**
     * handle on click of confirmation button
     */
    protected void setUpConfirmPostButton(@NonNull View parentView, @NonNull Context context){
        Button confirmPostButton = parentView.findViewById(R.id.addJobConfirmButton);
        confirmPostButton.setOnClickListener(view -> {
            // check fields
            String errorMessage = checkAllFields(parentView);
            TextView status = parentView.findViewById(R.id.jobSubmitStatus);

            if(!"".equals(errorMessage)){
                // handle error message
                status.setText(errorMessage);
            }
            else{
                // save to db
                createJob(parentView, context);
                // write success message
                status.setText(R.string.success);
                // move to next page
            }

        });
    }

    /**
     * Initiate duration spinner
     */
    protected void setUpDurationSpinner(@NonNull View parentView, @NonNull Context context){
        Spinner durationSpinner = parentView.findViewById(R.id.jobDurationSpinner);

        String[] durationValues = getResources().getStringArray(R.array.durationSpinnerValues);

        ArrayAdapter<String> durationAA = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, durationValues);
        durationAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAA);
    }

    /**
     * Initiate urgency spinner
     */
    protected void setUpUrgencySpinner(@NonNull View parentView, @NonNull Context context){
        Spinner urgencySpinner = parentView.findViewById(R.id.jobUrgencySpinner);

        String[] urgencyValues = getResources().getStringArray(R.array.urgencySpinnerValues);

        ArrayAdapter<String> urgencyAA = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, urgencyValues);
        urgencyAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgencySpinner.setAdapter(urgencyAA);
    }

    /**
     * Initiate province spinner
     */
    protected void setUpProvinceSpinner(@NonNull View parentView, @NonNull Context context){
        Spinner provinceSpinner = parentView.findViewById(R.id.addJobProvince);

        String[] provinceValues = getResources().getStringArray(R.array.provinceSpinnerValues);

        ArrayAdapter<String> provinceAA = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, provinceValues);
        provinceAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAA);
    }

    /**
     * Method to check that all input values are valid
     * @return A string holding an error message; empty when no input errors
     */
    protected @NonNull String checkAllFields(@NonNull View parentView){
        // check fields and return error message?
        Map<String, String> fields = getFieldsMap(parentView);
        return PostJobFormFields.checkFieldsValid(fields);
    }

    /**
     * Creates a new available job in the database
     */
    protected void createJob(@NonNull View parentView, @NonNull Context context) {
        Map<String, String> fields = getFieldsMap(parentView);
        AvailableJob job = PostAvailableJobHelper.createAvailableJob(fields, context);
        String key = job.writeToDatabase(database, error-> Log.e("PostJobForm", error));
        Log.d(LOG_TAG, "Job key: " + key);
    }

    // Getters
    protected @NonNull String getJobTitle(@NonNull View parentView){
        EditText jobTitle = parentView.findViewById(R.id.jobPostingTitle);
        return jobTitle.getText().toString().trim();
    }
    protected @NonNull String getJobDate(@NonNull View parentView){
        EditText jobDate = parentView.findViewById(R.id.addJobDate);
        return jobDate.getText().toString().trim();
    }
    protected @NonNull String getJobSalary(@NonNull View parentView){
        EditText jobSalary = parentView.findViewById(R.id.addJobSalary);
        return jobSalary.getText().toString().trim();
    }
    protected @NonNull String getJobAddress(@NonNull View parentView){
        EditText jobAddress = parentView.findViewById(R.id.addJobAddress);
        return jobAddress.getText().toString().trim();
    }
    protected @NonNull String getJobCity(@NonNull View parentView){
        EditText jobCity = parentView.findViewById(R.id.addJobCity);
        return jobCity.getText().toString().trim();
    }
    protected @NonNull String getJobDescription(@NonNull View parentView){
        EditText jobDescription = parentView.findViewById(R.id.addJobDescription);
        return jobDescription.getText().toString().trim();
    }
    protected @NonNull String getDuration(@NonNull View parentView) {
        Spinner jobDuration = parentView.findViewById(R.id.jobDurationSpinner);
        return jobDuration.getSelectedItem().toString();
    }
    protected @NonNull String getUrgency(@NonNull View parentView) {
        Spinner jobUrgency = parentView.findViewById(R.id.jobUrgencySpinner);
        return jobUrgency.getSelectedItem().toString();
    }
    protected @NonNull String getProvince(@NonNull View parentView) {
        Spinner jobProvince = parentView.findViewById(R.id.addJobProvince);
        return jobProvince.getSelectedItem().toString();
    }

    /**
     * Method to create a hashmap with all the fields as the keys and the user input as values
     * @return a hashmap
     */
    protected @NonNull Map<String, String> getFieldsMap(@NonNull View parentView){
        Map<String, String> fields = new HashMap<>();

        fields.put("title", getJobTitle(parentView));
        fields.put("date", getJobDate(parentView));
        fields.put("salary", getJobSalary(parentView));
        fields.put("address", getJobAddress(parentView));
        fields.put("city", getJobCity(parentView));
        fields.put("province", getProvince(parentView));
        fields.put("duration", getDuration(parentView));
        fields.put("urgency", getUrgency(parentView));
        fields.put("description", getJobDescription(parentView));
        return fields;
    }
}
