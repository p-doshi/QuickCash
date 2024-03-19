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

public final class PostAvailableJobHelper {

    // utility class
    private PostAvailableJobHelper(){}
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

    private static Address locToCoordinates(String streetAdd, String city, String province, Context context) {
        Geocoder geocoder = new Geocoder(context);
        String strAddress = streetAdd + ", " + city + ", " + province + ", Canada";
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(strAddress, 20);
        } catch (IOException e) {
            Log.e("PostAvailableJobHelper", Objects.requireNonNull(e.getMessage()));
        }
        assert address != null;
        return address.get(0);
    }

    private static double salaryStringToDouble(String strSalary){
        return Double.parseDouble(strSalary);
    }

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
