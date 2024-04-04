package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class Worker extends User {
    public static final String DIR = "public/workers/";
    private String summary;
    private List<String> skills;

    @VisibleForTesting
    public static @NonNull Worker createForTest(@NonNull String key) {
        Worker worker = new Worker();
        worker.key(key);
        return worker;
    }

    public @Nullable String getSummary() {
        return summary;
    }

    public void setSummary(@NonNull String summary) {
        this.summary = summary;
    }

    public @Nullable List<String> getSkills() {
        return skills;
    }

    public void setSkills(@NonNull List<String> skills) {
        this.skills = skills;
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
        @NonNull Consumer<Worker> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, Worker.class, worker -> {
                worker.key(key);
                readFunction.accept(worker);
            },
            errorFunction);
    }
}
