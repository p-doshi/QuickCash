package dal.cs.quickcash3.registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.RegisteringUser;
import dal.cs.quickcash3.data.User;
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
    private RegisteringUser registeringUser;

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
        formFields.add(new GeneralField(findViewById(R.id.firstName), R.string.invalid_first_name, registeringUser::setFirstName));
        formFields.add(new GeneralField(findViewById(R.id.lastName), R.string.invalid_last_name, registeringUser::setLastName));
        formFields.add(new AddressField(findViewById(R.id.address), registeringUser::setAddress));
        formFields.add(new DateField(findViewById(R.id.birthYear), findViewById(R.id.birthMonth), findViewById(R.id.birthDay), registeringUser::setBirthDate));
        formFields.add(new EmailAddressField(findViewById(R.id.emailAddress), registeringUser::setEmail));
        formFields.add(new PasswordField(findViewById(R.id.password), findViewById(R.id.confirmPassword), registeringUser::setPassword));
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
                field.retrieve();
            }
            // if all text valid, set view to valid
            statusTextView.setText(R.string.registration_successful);
            moveToChooseRoleWindow();
        } catch (FieldValidationException e) {
            statusTextView.setText(e.getErrorMessageId());
        }
    }

    private void moveToChooseRoleWindow() {
        Intent chooseRoleIntent = new Intent(getBaseContext(), ChooseRoleActivity.class);
        chooseRoleIntent.putExtra("User", registeringUser);
        startActivity(chooseRoleIntent);
        finish();
    }
}