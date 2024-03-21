package dal.cs.quickcash3.employer;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

/** @author Hayley Vezeau
 * Utility class to create an Available Job
 */
public final class PostAvailableJobHelper {

    private PostAvailableJobHelper(){}

    /**
     * Create an Available Job
     * @param fields a Map that holds the fields as keys and user input as values
     * @param context the context for the activity
     * @return an Available job object for the newly created Availble Job
     */
    public static @NonNull AvailableJob createAvailableJob(@NonNull Map<String, String> fields, @NonNull Context context) {
        AvailableJob job = new AvailableJob();

        job.setTitle(Objects.requireNonNull(fields.get("title")));
        job.setStartDate(Objects.requireNonNull(fields.get("date")));
        job.setUrgency(Objects.requireNonNull(fields.get("urgency")));
        job.setDuration(durationToDouble(Objects.requireNonNull(fields.get("duration"))));
        job.setSalary(salaryStringToDouble(fields.get("salary")));

        job.setDescription(Objects.requireNonNull(fields.get("description")));

        Address address = locToCoordinates(fields.get("address"), fields.get("city"), fields.get("province"), context);
        job.setLatitude(address.getLatitude());
        job.setLongitude(address.getLongitude());


        //TODO add employer ID
        job.setEmployer(RandomStringGenerator.generate(30));
        job.setPostTime(new Date().toString());
        job.setApplicants(new ArrayList<>());
        job.setBlackList(new ArrayList<>());

        return job;
    }

    /**
     * Convert full string address to Address object
     * @param streetAdd a String representing the street address
     * @param city a String representing the city
     * @param province a String representing a province
     * @param context the context of the activity
     * @return the address as an Address object
     */
    private static Address locToCoordinates(String streetAdd, String city, String province, Context context) {
        Geocoder geocoder = new Geocoder(context);
        String strAddress = streetAdd + ", " + city + ", " + province + ", Canada";
        List<Address> address;
        try {
            do {
                address = geocoder.getFromLocationName(strAddress, 20);
            }
            while (address == null);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return address.get(0);
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
