package dal.cs.quickcash3.data;

import static dal.cs.quickcash3.util.CopyHelper.copyTo;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.Date;
import java.util.Objects;
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

    public static @NonNull CompletedJob completeJob(@NonNull AvailableJob availableJob, @NonNull Worker worker) {
        CompletedJob completedJob = new CompletedJob();
        copyTo(completedJob, availableJob);
        completedJob.key(Objects.requireNonNull(availableJob.key()));
        completedJob.setWorker(Objects.requireNonNull(worker.key()));
        completedJob.setCompletionDate(new Date().toString());
        return completedJob;
    }

    @Override
    public @NonNull String toString() {
        return super.toString() +
            "\nworker='" + worker + '\'' +
            "\ncompletionDate='" + completionDate + '\'' +
            "\npayId='" + payId + '\'';
    }

    @Override
    public void writeToDatabase(
        @NonNull Database database,
        @NonNull Consumer<String> errorFunction)
    {
        writeToDatabase(database, () -> {}, errorFunction);
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
    public void deleteFromDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        if (key() == null) {
            throw new IllegalArgumentException("Job doesn't exist");
        }
        database.delete(DIR + key(), errorFunction);
    }

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<CompletedJob> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, CompletedJob.class, job -> {
                job.key(key);
                readFunction.accept(job);
            },
            errorFunction);
    }
}
