package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseWriter;

public abstract class User implements DatabaseWriter {
    protected static final int HASH_SIZE = 20;
    public static final String DIR = "public/users/";
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;

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

    public @Nullable String getWholeName() {
        if (firstName != null && lastName != null) {
            return firstName + ' ' + lastName;
        }
        else {
            return null;
        }
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

    public @Nullable String getRole() {
        return role;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<User> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, User.class,
            user -> {
                // A subsequent read from the same location should be almost instant.
                if (UserRole.EMPLOYER.getValue().equals(user.role)) {
                    database.read(location, Employer.class, readFunction::accept, errorFunction);
                }
                else if (UserRole.WORKER.getValue().equals(user.role)) {
                    database.read(location, Worker.class, readFunction::accept, errorFunction);
                }
                else {
                    errorFunction.accept("User does not have a role");
                }
            },
            errorFunction);
    }
}
