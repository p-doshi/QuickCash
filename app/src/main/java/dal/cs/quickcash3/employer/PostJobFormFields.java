package dal.cs.quickcash3.employer;

import java.util.Map;
import java.util.regex.Pattern;

public final class PostJobFormFields {
    static final String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((20|2[0-9])[0-9]{2})$";
    static final String SALARY_PATTERN = "^([0-9]+|[0-9]+\\.[0-9]{2})$";
    static final String EMPTY = "";
    static final String MISSING_FIELD_ERROR = "Please fill in all fields!";
    static final String IMPROPER_DATE_FORMAT = "Please enter date in proper format - dd/mm/yyyy";
    static final String IMPROPER_SALARY_FORMAT = "Please enter valid salary";

    /**
     * Method for PostJobForm to call to check form fields properly filled out
     * @param fields a hashmap containing fields as key and user input as the values
     * @return A string holding an error message; empty when no errors
     */
    public static String checkFieldsValid(Map<String, String> fields){
        String errorMessage = EMPTY;
        String emptyError = checkIfEmpty(fields);
        String dateFormatError = checkJobDate(fields.get("date"));
        String salaryFormatError = checkJobSalary(fields.get("salary"));

        if(!EMPTY.equals(emptyError)){
            errorMessage = emptyError;
        }
        else if (!EMPTY.equals(dateFormatError)) {
            errorMessage = dateFormatError;
        }
        else if (!EMPTY.equals(salaryFormatError)) {
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
        String errorMessage = EMPTY;

        for (Map.Entry<String, String> field : fields.entrySet()) {
            String key = field.getKey();
            String input = field.getValue();
            String provinceStr = "province";
            String durationStr = "duration";
            String urgencyStr = "urgency";

            boolean provinceCheck = key.equals(provinceStr) && input.equals(provinceStr);
            boolean durationCheck = key.equals(durationStr) && input.equals(durationStr);
            boolean urgencyCheck = key.equals(urgencyStr) && input.equals(urgencyStr);

            if(EMPTY.equals(input)||provinceCheck||durationCheck||urgencyCheck){
                errorMessage = MISSING_FIELD_ERROR;
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
        String errorMessage = EMPTY;

        if(!Pattern.matches(DATE_PATTERN, date)){
            errorMessage = IMPROPER_DATE_FORMAT;
        }
        return errorMessage;
    }

    /**
     * Method to verify salary was input in proper format
     * @param salary a string representing the salary
     * @return an error message string for proper formatting; empty string if no formatting error
     */
    private static String checkJobSalary(String salary){
        String errorMessage = EMPTY;

        if(!Pattern.matches(SALARY_PATTERN, salary)){
            errorMessage = IMPROPER_SALARY_FORMAT;
        }
        return errorMessage;
    }
}
