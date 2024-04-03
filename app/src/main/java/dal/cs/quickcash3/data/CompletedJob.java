package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class CompletedJob extends JobPost {
    public static final String DIR = "public/completed_jobs/";
    private String worker;
    private String completionDate;
    private String payId;
    private String status;

    public @Nullable String getWorker() {
        return worker;
    }

    public void setWorker(@NonNull String worker) {
        this.worker = worker;
    }

    public @Nullable String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(@NonNull String completionDate) {
        this.completionDate = completionDate;
    }

    public @Nullable String getPayId() {
        return payId;
    }

    public void setPayId(@NonNull String payId) {
        this.payId = payId;
    }

    public @Nullable String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @Override
    public @NonNull String toString() {
        return super.toString() +
            "\nworker='" + worker + '\'' +
            "\ncompletionDate='" + completionDate + '\'' +
            "\npayId='" + payId + '\'';
    }

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

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<CompletedJob> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, CompletedJob.class, readFunction, errorFunction);
    }
}
