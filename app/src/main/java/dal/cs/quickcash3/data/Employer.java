package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class Employer extends User {
    @Override
    public @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Consumer<String> errorFunction)
    {
        return writeToDatabase(database, () -> {}, errorFunction);
    }

    @Override
    public @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String key = RandomStringGenerator.generate(HASH_SIZE);
        writeToDatabase(database, key, successFunction, errorFunction);
        return key;
    }

    @Override
    public void writeToDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<String> errorFunction)
    {
        writeToDatabase(database, key, () -> {}, errorFunction);
    }

    @Override
    public void writeToDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.write(
            DIR + key,
            this,
            successFunction,
            errorFunction);
    }
}
