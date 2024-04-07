package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class Employer extends User {
    public static final String DIR = "public/employers/";

    @VisibleForTesting
    public static @NonNull Employer createForTest(@NonNull String key) {
        Employer employer = new Employer();
        employer.key(key);
        return employer;
    }

    @Override
    public void writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        if (key() == null) {
            key(RandomStringGenerator.generate(HASH_SIZE));
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
            throw new IllegalArgumentException("User doesn't exist");
        }
        database.delete(DIR + key(), successFunction, errorFunction);
        key(null);
    }

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<Employer> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, Employer.class, worker -> {
                worker.key(key);
                readFunction.accept(worker);
            },
            errorFunction);
    }
}
