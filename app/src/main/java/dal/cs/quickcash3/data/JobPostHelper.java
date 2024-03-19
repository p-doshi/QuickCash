package dal.cs.quickcash3.data;

import static dal.cs.quickcash3.location.LocationHelper.randomLocation;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import dal.cs.quickcash3.util.RandomStringGenerator;

public final class JobPostHelper {
    private static final Random RANDOM = new Random();

    // Utility class.
    private JobPostHelper() {}

    private static double roundToNearestCent(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    /**
     * Generate a list of available job posts with a set size. The jobs will be randomly scattered
     * over the area.
     *
     * @param number The number of job posts to generate.
     * @param area The area to randomly place jobs.
     * @return A list of job posts in the area.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // That is the point of this method.
    public static @NonNull List<AvailableJob> generateAvailable(int number, @NonNull LatLngBounds area) {
        List<AvailableJob> jobs = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            AvailableJob job = new AvailableJob();
            job.setTitle(RandomStringGenerator.generate(10));
            job.setDescription(RandomStringGenerator.generate(40));
            job.setEmployer(RandomStringGenerator.generate(30));
            job.setPostTime(new Date().toString());
            job.setApplicants(new ArrayList<>());
            job.setBlackList(new ArrayList<>());
            job.setSalary(roundToNearestCent(RANDOM.nextDouble() * 1000.0));

            LatLng location = randomLocation(area);
            job.setLatitude(location.latitude);
            job.setLongitude(location.longitude);

            jobs.add(job);
        }
        return jobs;
    }
}
