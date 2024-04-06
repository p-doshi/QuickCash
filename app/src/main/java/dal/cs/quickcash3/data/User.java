package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dal.cs.quickcash3.database.DatabaseObject;

@SuppressWarnings("PMD.ShortClassName") // I think it's fine.
public abstract class User extends DatabaseObject {
    protected static final int HASH_SIZE = 30;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public @Nullable String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public @Nullable String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public @Nullable String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public @Nullable String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public @Nullable String fullName() {
        if (firstName != null && lastName != null) {
            return firstName + ' ' + lastName;
        }
        else {
            return null;
        }
    }

    @Override
    public @NonNull String toString() {
        return
            "\nkey='" + key() + '\'' +
            "\nfirstName='" + firstName + '\'' +
            "\nlastName='" + lastName + '\'' +
            "\nemail='" + email + '\'' +
            "\nphone=" + phone;
    }
}
