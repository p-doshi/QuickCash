package dal.cs.quickcash3.employer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import dal.cs.quickcash3.R;

public class PostJobForm extends Activity {

    //How to import/use firebase database
    //MyFirebaseDatabase db = new MyFirebaseDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_job);

        // initialize spinners
        this.setUpDurationSpinner();
        this.setUpUrgencySpinner();
        this.setUpProvinceSpinner();

        this.setUpConfirmPostButton();
    }
    protected void setUpConfirmPostButton(){
        Button confirmPostButton = findViewById(R.id.addJobConfirmButton);
        confirmPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check fields
                String errorMessage = checkAllFields();

                // save information - call to separate db class???

            }
        });
    }

    protected void setUpDurationSpinner(){
        Spinner durationSpinner = findViewById(R.id.jobDurationSpinner);

        String[] durationValues = getResources().getStringArray(R.array.durationSpinnerValues);

        ArrayAdapter<String> durationAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durationValues);
        durationAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAA);
    }

    protected void setUpUrgencySpinner(){
        Spinner urgencySpinner = findViewById(R.id.jobUrgencySpinner);

        String[] urgencyValues = getResources().getStringArray(R.array.urgencySpinnerValues);

        ArrayAdapter<String> urgencyAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urgencyValues);
        urgencyAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgencySpinner.setAdapter(urgencyAA);
    }

    protected void setUpProvinceSpinner(){
        Spinner provinceSpinner = findViewById(R.id.addJobProvince);

        String[] provinceValues = getResources().getStringArray(R.array.provinceSpinnerValues);

        ArrayAdapter<String> provinceAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceValues);
        provinceAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAA);
    }

    protected String checkAllFields(){
        String errorMessage = "";
        // check fields and return error message?
        return errorMessage;
    }

    // Getters
    protected String getJobTitle(){
        EditText jobTitle = findViewById(R.id.jobPostingTitle);
        return jobTitle.getText().toString().trim();
    }
    protected String getJobDate(){
        EditText jobDate = findViewById(R.id.addJobDate);
        return jobDate.getText().toString().trim();
    }
    protected String getJobSalary(){
        EditText jobSalary = findViewById(R.id.addJobSalary);
        return jobSalary.getText().toString().trim();
    }
    protected String getJobAddress(){
        EditText jobAddress = findViewById(R.id.addJobAddress);
        return jobAddress.getText().toString().trim();
    }
    protected String getJobCity(){
        EditText jobCity = findViewById(R.id.addJobCity);
        return jobCity.getText().toString().trim();
    }
    protected String getJobDescription(){
        EditText jobDescription = findViewById(R.id.addJobDescription);
        return jobDescription.getText().toString().trim();
    }
    protected String getDuration() {
        Spinner jobDuration = findViewById(R.id.jobDurationSpinner);
        return jobDuration.getSelectedItem().toString();
    }
    protected String getUrgency() {
        Spinner jobUrgency = findViewById(R.id.jobUrgencySpinner);
        return jobUrgency.getSelectedItem().toString();
    }
    protected String getProvince() {
        Spinner jobProvince = findViewById(R.id.addJobProvince);
        return jobProvince.getSelectedItem().toString();
    }
}
