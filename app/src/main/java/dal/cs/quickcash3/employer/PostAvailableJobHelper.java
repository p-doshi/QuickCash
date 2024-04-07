package dal.cs.quickcash3.employer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.PostalAddress;
import dal.cs.quickcash3.geocode.MyGeocoder;
import dal.cs.quickcash3.util.RandomStringGenerator;

/** @author Hayley Vezeau
 * Utility class to create an Available Job
 */
public final class PostAvailableJobHelper {

    private PostAvailableJobHelper(){}

    /**
     * Creates an Available Job. Since this requires reverse geocoding, this function requires a
     * lambda to receive the created job.
     *
     * @param geocoder the geocoder used by the activity
     * @param fields a Map that holds the fields as keys and user input as values
     * @param receiverFunction the function to receive the created job.
     */
    public static void createAvailableJob(
        @NonNull MyGeocoder geocoder,
        @NonNull Map<String, String> fields,
        @NonNull Consumer<AvailableJob> receiverFunction,
        @NonNull Consumer<String> errorFunction)
    {
        AvailableJob job = new AvailableJob();

        job.setTitle(Objects.requireNonNull(fields.get("title")));
        job.setStartDate(Objects.requireNonNull(fields.get("date")));
        job.setUrgency(Objects.requireNonNull(fields.get("urgency")));
        job.setDuration(durationToDouble(Objects.requireNonNull(fields.get("duration"))));
        job.setSalary(salaryStringToDouble(fields.get("salary")));

        job.setDescription(Objects.requireNonNull(fields.get("description")));

        //TODO add employer ID
        job.setEmployer(RandomStringGenerator.generate(30));
        job.setPostTime(new Date().toString());
        job.setApplicants(new ArrayList<>());
        job.setRejectants(new ArrayList<>());

        PostalAddress address = PostalAddress.createCanadianAddress(
            Objects.requireNonNull(fields.get("address")),
            Objects.requireNonNull(fields.get("city")),
            Objects.requireNonNull(fields.get("province")));
        geocoder.fetchLocationFromAddress(address.toString(),
            location -> {
                job.setLatitude(location.latitude);
                job.setLongitude(location.longitude);
                receiverFunction.accept(job);
            },
            errorFunction);
    }

    /**
     * convert the salary to a double of the same value
     * @param strSalary a String representing the salary
     * @return a double of the salary amount
     */
    private static double salaryStringToDouble(String strSalary){
        return Double.parseDouble(strSalary);
    }

    /**
     * convert the duration to double to allow for filtering
     * @param duration a String representing the timeframe
     * @return a double used as a proxy for duration
     */
    private static double durationToDouble(String duration) {
        double doubleDuration = 0;

        String under8Hours = "Under 8 Hours";
        String days1to3 = "1 – 3 Days";
        String days4to7 = "4 – 7 Days";
        String weeks1to2 = "1 – 2 Weeks";
        String weeks2to4 = "2 – 4 Weeks";
        String weeks4plus = "4+ Weeks";

        if (under8Hours.equals(duration)) {
            doubleDuration = 8;
        } else if (days1to3.equals(duration)) {
            doubleDuration = 24;
        } else if (days4to7.equals(duration)) {
            doubleDuration = 56;
        } else if (weeks1to2.equals(duration)) {
            doubleDuration = 112;
        } else if (weeks2to4.equals(duration)) {
            doubleDuration = 240;
        } else if (weeks4plus.equals(duration)) {
            doubleDuration = 336;
        }

        return doubleDuration;
    }

}
