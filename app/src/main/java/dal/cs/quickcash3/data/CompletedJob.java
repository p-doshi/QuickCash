package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class CompletedJob extends JobPost {
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

    public @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Consumer<String> errorFunction)
    {
        return writeToDatabase(database, () -> {}, errorFunction);
    }

    public @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String key = RandomStringGenerator.generate(HASH_SIZE);
        database.write(
            DatabaseDirectory.COMPLETED_JOBS.getValue() + key,
            this,
            successFunction,
            errorFunction);
        return key;
    }

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<CompletedJob> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DatabaseDirectory.COMPLETED_JOBS.getValue() + key;
        database.read(location, CompletedJob.class, readFunction, errorFunction);
    }
}
