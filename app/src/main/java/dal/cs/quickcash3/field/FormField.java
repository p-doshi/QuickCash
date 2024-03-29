package dal.cs.quickcash3.field;

import dal.cs.quickcash3.registration.FieldValidationException;

public interface FormField {
    void validate() throws FieldValidationException;

}
