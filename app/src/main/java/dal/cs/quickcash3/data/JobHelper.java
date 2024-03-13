package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.Random;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.util.RandomStringGenerator;

public final class JobHelper {
    // Utility class.
    private JobHelper() {}

    private static float roundToNearestCent(float val) {
        return Math.round(val * 100.0f) / 100.0f;
    }

    private static double scaleNormalized(double normal, double min, double max) {
        return normal * (max - min) + min;
    }

    public static @NonNull List<AvailableJob> generateAvailable(int number, @NonNull LatLngBounds area) {
        Random random = new Random(0); // Consistent randomness.
        List<AvailableJob> jobs = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            AvailableJob job = new AvailableJob();
            job.setTitle(RandomStringGenerator.generate(10));
            job.setDescription(RandomStringGenerator.generate(40));
            job.setEmployerId(RandomStringGenerator.generate(30));
            job.setPostTime(new Date().toString());
            job.setApplicants(new ArrayList<>());
            job.setBlackList(new ArrayList<>());

            float pay = roundToNearestCent(random.nextFloat() * 1000.0f);
            job.setPay(String.valueOf(pay));

            double lat = scaleNormalized(random.nextDouble(), area.southwest.latitude, area.northeast.latitude);
            double lng = scaleNormalized(random.nextDouble(), area.southwest.longitude, area.northeast.longitude);
            LatLng location = new LatLng(lat, lng);
            job.setLocation(location);

            jobs.add(job);
        }
        return jobs;
    }

    public static <J extends Job> void postJobs(@NonNull Database database, @NonNull List<J> jobs, @NonNull Consumer<String> errorFunction) {
        if (jobs.isEmpty()) {
            return;
        }

        J first = jobs.get(0);
        String directory;
        if (first instanceof AvailableJob) {
            directory = DatabaseDirectory.AVAILABLE_JOBS.getValue();
        }
        else if (first instanceof CompletedJob) {
            directory = DatabaseDirectory.COMPLETED_JOBS.getValue();
        }
        else {
            throw new IllegalArgumentException("Unrecognized job type: " + first.getClass());
        }

        for (J job : jobs) {
            database.write(directory, job, errorFunction);
        }
    }
}
