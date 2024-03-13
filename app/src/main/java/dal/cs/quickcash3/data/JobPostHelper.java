package dal.cs.quickcash3.data;

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

    private static float roundToNearestCent(float val) {
        return Math.round(val * 100.0f) / 100.0f;
    }

    private static double scaleNormalized(double normal, double min, double max) {
        return normal * (max - min) + min;
    }

    /**
     * Get a random location from within a given area.
     *
     * @param area The area to pick a random location from.
     * @return A random location.
     */
    @SuppressWarnings("PMD.LawOfDemeter") // There is no other way to do this.
    public static @NonNull LatLng randomLocation(@NonNull LatLngBounds area) {
        double lat = scaleNormalized(RANDOM.nextDouble(), area.southwest.latitude, area.northeast.latitude);
        double lng = scaleNormalized(RANDOM.nextDouble(), area.southwest.longitude, area.northeast.longitude);
        return new LatLng(lat, lng);
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
            job.setEmployerId(RandomStringGenerator.generate(30));
            job.setPostTime(new Date().toString());
            job.setApplicants(new ArrayList<>());
            job.setBlackList(new ArrayList<>());

            float pay = roundToNearestCent(RANDOM.nextFloat() * 1000.0f);
            job.setPay(String.valueOf(pay));

            job.setLocation(randomLocation(area));

            jobs.add(job);
        }
        return jobs;
    }
}
