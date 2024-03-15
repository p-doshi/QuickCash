package dal.cs.quickcash3.employer;

import android.content.Context;

import java.util.HashMap;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;

public class PostJobFormFields {
    //static Context context; // fix this during refactoring
    static String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((20|2[0-9])[0-9]{2})$";
    static String salaryPattern = "[0-9]+|[0-9]+\\.\\d\\d";
    static String empty = "";
    static String errorMessage;
    static String missingFieldError = "Please fill in all fields!";
    static String improperDateFormat = "Please enter date in proper format - dd/mm/yyyy";
    static String improperSalaryFormat = "Please enter valid salary";

    /**
     * Method for PostJobForm to call to check form fields properly filled out
     * @param fields a hashmap containing fields as key and user input as the values
     * @return A string holding an error message; empty when no errors
     */
    public static String checkFieldsValid(HashMap<String, String> fields){
        errorMessage = checkIfEmpty(fields);
        if(errorMessage.equals(empty)){
            errorMessage = checkJobDate(fields.get("date"));
            errorMessage = checkJobSalary(fields.get("salary"));
        }

        return errorMessage;
    }

    /**
     * Checks to see if any form fields are empty (i.e. have not been filled out)
     * @param fields a hashmap containing fields as key and user input as the values
     * @return A string holding an error message; empty when no errors
     */
    private static String checkIfEmpty(HashMap<String, String> fields){
        errorMessage = empty;

        for (HashMap.Entry<String, String> field : fields.entrySet()) {
            String input = (String)field.getValue();

            if(input.equals(empty)){
                errorMessage = missingFieldError;
                break;
            }
        }
        return errorMessage;
    }

    /**
     * Method to verify date was entered in proper format
     * @param date the date as input by the user
     * @return an error message string for reformatting; empty string if no formatting error
     */
    public static String checkJobDate(String date){
        errorMessage = empty;

        if(!Pattern.matches(datePattern, date)){
            errorMessage = improperDateFormat;
        }
        return errorMessage;
    }

    /**
     * Method to verify salary was input in proper format
     * @param salary a string representing the salary
     * @return an error message string for proper formatting; empty string if no formatting error
     */
    public static String checkJobSalary(String salary){
        errorMessage = empty;

        if(!Pattern.matches(salaryPattern, salary)){
            errorMessage = improperSalaryFormat;
        }
        return errorMessage;
    }
}
