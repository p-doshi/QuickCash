package dal.cs.quickcash3.employer;

import android.content.Context;

import java.util.regex.Pattern;

import dal.cs.quickcash3.R;

public class PostJobFormFields {
    Context context;
    String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((20|2[0-9])[0-9]{2})$";
    String salaryPattern = "[0-9]+\\.\\d\\d";
    String empty = "";

    private String checkIfEmpty(String input){
        String errorMessage = empty;

        if(input.equals(empty)){
            errorMessage = context.getString(R.string.fillAllFields);
        }
        return errorMessage;
    }

    // How to best return error information???
    // How to best document code???
    public void checkJobTitle(String title){
        // is this the best way to do this??
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(title);
    }
    public void checkJobDate(String date){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(date);

        if(!Pattern.matches(datePattern, date)){
            errorMessage = context.getString(R.string.dateError);
        }
    }
    public void checkJobSalary(String salary){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(salary);

        if(!Pattern.matches(salaryPattern, salary)){
            errorMessage = context.getString(R.string.salaryError);
        }
    }
    public void checkJobAddress(String address){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(address);
    }
    public void checkJobCity(String city){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(city);
    }
    public void checkJobDescription(String description){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(description);
    }
    public void checkJobDuration(String duration){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(duration);
    }
    public void checkJobUrgency(String urgency){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(urgency);
    }
    public void checkJobProvince(String province){
        String errorMessage = empty;
        errorMessage = this.checkIfEmpty(province);
    }
}
