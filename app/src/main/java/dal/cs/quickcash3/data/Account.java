package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseObject;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class Account extends DatabaseObject {
    public static final String DIR = "private/users/";
    private String role;
    private String id;

    public static Account create(@NonNull String key) {
        Account account = new Account();
        account.key(key);
        return account;
    }

    public @Nullable String getRole() {
        return role;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    public @Nullable String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public void writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        if (key() == null) {
            throw new IllegalArgumentException("Account missing User ID");
        }
        database.write(
            DIR + key(),
            this,
            successFunction,
            errorFunction);
    }

    @Override
    public void deleteFromDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        if (key() == null) {
            throw new IllegalArgumentException("Account doesn't exist");
        }
        database.delete(DIR + key(), successFunction, errorFunction);
        key(null);
    }

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<Account> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, Account.class,
            account -> {
                account.key(key);
                readFunction.accept(account);
            },
            errorFunction);
    }
}
