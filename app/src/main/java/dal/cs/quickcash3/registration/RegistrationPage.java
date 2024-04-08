package dal.cs.quickcash3.registration;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.field.AddressField;
import dal.cs.quickcash3.field.DateField;
import dal.cs.quickcash3.field.EmailAddressField;
import dal.cs.quickcash3.field.FieldValidationException;
import dal.cs.quickcash3.field.FormField;
import dal.cs.quickcash3.field.GeneralField;
import dal.cs.quickcash3.field.PasswordField;


public class RegistrationPage extends AppCompatActivity {
    private final List<FormField> formFields = new ArrayList<>();
    private TextView statusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        init();
    }

    /**
     * Initializes UI components and sets up event listeners. It finds UI elements
     * by their IDs and sets an onClickListener for the confirmation button.
     */
    private void init(){
        formFields.add(new GeneralField(findViewById(R.id.firstName), R.string.invalid_first_name));
        formFields.add(new GeneralField(findViewById(R.id.lastName), R.string.invalid_last_name));
        formFields.add(new AddressField(findViewById(R.id.address)));
        formFields.add(new DateField(findViewById(R.id.birthYear), findViewById(R.id.birthMonth), findViewById(R.id.birthDay)));
        formFields.add(new EmailAddressField(findViewById(R.id.emailAddress)));
        formFields.add(new PasswordField(findViewById(R.id.password), findViewById(R.id.confirmPassword)));
        statusTextView = findViewById(R.id.registrationStatus);

        findViewById(R.id.confirmButton).setOnClickListener(v -> submitForm());
    }

    /**
     * Submits the registration form after validating all input fields. It validates
     * each field using {@code validateField} method and shows an error message
     * if any validation fails.
     */
    private void submitForm(){
        try {
            for (FormField field : formFields) {
                field.validate();
            }
            // if all text valid, set view to valid
            statusTextView.setText(R.string.registration_successful);
        } catch (FieldValidationException e) {
            statusTextView.setText(e.getErrorMessageId());
        }
    }
}