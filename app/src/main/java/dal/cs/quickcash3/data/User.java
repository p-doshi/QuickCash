package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseObject;

@SuppressWarnings("PMD.ShortClassName") // I think it's fine.
public class User extends DatabaseObject {
    protected static final int HASH_SIZE = 30;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String address;

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

    public @Nullable LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NonNull LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public @Nullable String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
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
            "\nbirth date='" + birthDate + '\''+
            "\naddress='" + address + '\'';
    }

    @Override
    public void writeToDatabase(@NonNull Database database, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        throw new IllegalStateException("Cannot write a User to database");
    }

    @Override
    public void deleteFromDatabase(@NonNull Database database, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        throw new IllegalStateException("Cannot delete a User from database");
    }
}
