package dal.cs.quickcash3.employer;

import java.util.Map;
import java.util.regex.Pattern;

public class PostJobFormFields {
    static final String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((20|2[0-9])[0-9]{2})$";
    static final String salaryPattern = "^([0-9]+|[0-9]+\\.[0-9]{2})$";
    static final String empty = "";
    static String errorMessage;
    static final String missingFieldError = "Please fill in all fields!";
    static final String improperDateFormat = "Please enter date in proper format - dd/mm/yyyy";
    static final String improperSalaryFormat = "Please enter valid salary";

    /**
     * Method for PostJobForm to call to check form fields properly filled out
     * @param fields a hashmap containing fields as key and user input as the values
     * @return A string holding an error message; empty when no errors
     */
    protected static String checkFieldsValid(Map<String, String> fields){
        errorMessage = empty;
        String emptyError = checkIfEmpty(fields);
        String dateFormatError = checkJobDate(fields.get("date"));
        String salaryFormatError = checkJobSalary(fields.get("salary"));

        if(!emptyError.equals(empty)){
            errorMessage = emptyError;
        }
        else if (!dateFormatError.equals(empty)) {
            errorMessage = dateFormatError;
        }
        else if (!salaryFormatError.equals(empty)) {
            errorMessage = salaryFormatError;
        }

        return errorMessage;
    }

    /**
     * Checks to see if any form fields are empty (i.e. have not been filled out)
     * @param fields a hashmap containing fields as key and user input as the values
     * @return A string holding an error message; empty when no errors
     */
    private static String checkIfEmpty(Map<String, String> fields){
        errorMessage = empty;

        for (Map.Entry<String, String> field : fields.entrySet()) {
            String key = field.getKey();
            String input = field.getValue();
            String provinceStr = "province";
            String durationStr = "duration";
            String urgencyStr = "urgency";

            boolean provinceCheck = key.equals(provinceStr) && input.equals(provinceStr);
            boolean durationCheck = key.equals(durationStr) && input.equals(durationStr);
            boolean urgencyCheck = key.equals(urgencyStr) && input.equals(urgencyStr);

            if(input.equals(empty)||provinceCheck||durationCheck||urgencyCheck){
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
    private static String checkJobDate(String date){
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
    private static String checkJobSalary(String salary){
        errorMessage = empty;

        if(!Pattern.matches(salaryPattern, salary)){
            errorMessage = improperSalaryFormat;
        }
        return errorMessage;
    }
}
