package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class AvailableJob extends JobPost {
    public static final String DIR = "public/available_jobs/";
    private String startDate;
    private double duration;
    private String urgency;
    private String postTime;
    private List<String> applicants;
    private List<String> blackList;

    @SuppressWarnings("PMD.UnnecessaryConstructor") // Empty constructor needed to read from Firebase.
    public AvailableJob() { 
        super();
    }

    public @Nullable String getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull String startDate) {
        this.startDate = startDate;
    }

    public @Nullable String getUrgency() {
        return urgency;
    }

    public void setUrgency(@NonNull String urgency) {
        this.urgency = urgency;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public @Nullable String getPostTime() {
        return postTime;
    }

    public void setPostTime(@NonNull String postTime) {
        this.postTime = postTime;
    }

    public @Nullable List<String> getApplicants() {
        return applicants;
    }

    public void setApplicants(@NonNull List<String> applicants) {
        this.applicants = applicants;
    }

    public @Nullable List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(@NonNull List<String> blackList) {
        this.blackList = blackList;
    }

    @Override
    public @NonNull String toString() {
        return super.toString() +
            "\nstartDate='" + startDate + '\'' +
            "\nduration=" + duration +
            "\nurgency='" + urgency + '\'' +
            "\npostTime='" + postTime + '\'' +
            "\napplicants=" + applicants +
            "\nblackList=" + blackList;
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
        @NonNull Consumer<AvailableJob> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.read(DIR + key, AvailableJob.class, readFunction, errorFunction);
    }
}
