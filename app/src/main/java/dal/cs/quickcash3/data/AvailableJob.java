package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
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
    private List<String> rejectants;

    @VisibleForTesting
    public static @NonNull AvailableJob createForTest(@NonNull String key) {
        AvailableJob job = new AvailableJob();
        job.key(key);
        return job;
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

    public @Nullable List<String> getRejectants() {
        return rejectants;
    }

    public void setRejectants(@NonNull List<String> rejectants) {
        this.rejectants = rejectants;
    }

    public @NonNull List<String> allApplicants() {
        List<String> all = new ArrayList<>();
        if (applicants != null) {
            all.addAll(applicants);
        }
        if (rejectants != null) {
            all.addAll(rejectants);
        }
        return all;
    }

    public boolean isApplicant(@Nullable String workerKey) {
        return applicants != null && applicants.contains(workerKey);
    }

    public boolean isRejectant(@Nullable String workerKey) {
        return rejectants != null && rejectants.contains(workerKey);
    }

    public void rejectWorker(@NonNull String workerKey) {
        if (applicants != null && applicants.contains(workerKey)) {
            if (rejectants == null) {
                rejectants = new ArrayList<>();
            }
            rejectants.add(workerKey);
            applicants.remove(workerKey);
        }
    }

    public void reconsiderWorker(@NonNull String workerKey) {
        if (rejectants != null && rejectants.contains(workerKey)) {
            if (applicants == null) {
                applicants = new ArrayList<>();
            }
            applicants.add(workerKey);
            rejectants.remove(workerKey);
        }
    }

    public  void  addApplicant(@NonNull String workerKey){
        if (applicants == null) {
            applicants = new ArrayList<>();
        }
        applicants.add(workerKey);
    }

    @Override
    public @NonNull String toString() {
        return super.toString() +
            "\nstartDate='" + startDate + '\'' +
            "\nduration=" + duration +
            "\nurgency='" + urgency + '\'' +
            "\npostTime='" + postTime + '\'' +
            "\napplicants=" + applicants +
            "\nblackList=" + rejectants;
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
            throw new IllegalArgumentException("Job doesn't exist");
        }
        database.delete(DIR + key(), successFunction, errorFunction);
        key(null);
    }

    public static void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<AvailableJob> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        String location = DIR + key;
        database.read(location, AvailableJob.class, job -> {
                job.key(key);
                readFunction.accept(job);
            },
            errorFunction);
    }
}
