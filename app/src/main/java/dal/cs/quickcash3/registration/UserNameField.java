package dal.cs.quickcash3.registration;

import android.widget.EditText;

import dal.cs.quickcash3.R;

public class UserNameField implements RegistrationFormField{
    private EditText editText;

    public UserNameField(EditText editText) {
        this.editText = editText;
    }


    @Override
    public void isValid() throws FieldValidationException {
        String userName = editText.getText().toString().trim();
        if (userName.isEmpty()) {
            throw new FieldValidationException(R.string.invalid_user_name);
        }
    }
}
