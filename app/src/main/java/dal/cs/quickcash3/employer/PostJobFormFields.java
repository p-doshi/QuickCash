package dal.cs.quickcash3.employer;

import android.content.Context;

import java.util.HashMap;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;

public class PostJobFormFields {
    Context context;
    String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((20|2[0-9])[0-9]{2})$";
    String salaryPattern = "[0-9]+\\.\\d\\d";
    String empty = "";
    String errorMessage;

    public String checkFieldsValid(HashMap<String, String> fields){
        errorMessage = checkIfEmpty(fields);
        if(errorMessage.equals(empty)){
            errorMessage = checkJobDate(fields.get("date"));
            errorMessage = checkJobSalary(fields.get("salary"));
        }

        return errorMessage;
    }

    private String checkIfEmpty(HashMap<String, String> fields){
        errorMessage = empty;

        for (HashMap.Entry<String, String> field : fields.entrySet()) {
            String input = (String)field.getValue();

            if(input.equals(empty)){
                errorMessage = context.getString(R.string.fillAllFields);
                break;
            }
        }
        return errorMessage;
    }

    // How to best return error information???
    // How to best document code???
    public String checkJobDate(String date){
        errorMessage = empty;

        if(!Pattern.matches(datePattern, date)){
            errorMessage = context.getString(R.string.dateError);
        }
        return errorMessage;
    }
    public String checkJobSalary(String salary){
        errorMessage = empty;

        if(!Pattern.matches(salaryPattern, salary)){
            errorMessage = context.getString(R.string.salaryError);
        }
        return errorMessage;
    }
}
