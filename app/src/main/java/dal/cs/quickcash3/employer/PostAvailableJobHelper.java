package dal.cs.quickcash3.employer;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class PostAvailableJobHelper {
    private Context context;
    private final Geocoder geocoder = new Geocoder(context);
    private Database database;

    public void createAvailableJob(HashMap<String, String> fields) throws IOException {
        AvailableJob job = new AvailableJob();

        job.setTitle(Objects.requireNonNull(fields.get("title")));
        job.setStartDate(Objects.requireNonNull(fields.get("date")));
        job.setUrgency(Objects.requireNonNull(fields.get("urgency")));
        job.setDuration(durationToDouble(Objects.requireNonNull(fields.get("duration"))));
        job.setSalary(salaryStringToDouble(fields.get("salary")));

        job.setDescription(Objects.requireNonNull(fields.get("description")));

        Address address = locToCoordinates(fields.get("address"), fields.get("city"), fields.get("province"));
        job.setLatitude(address.getLatitude());
        job.setLongitude(address.getLongitude());


        job.setEmployer(RandomStringGenerator.generate(30)); //how to do this???
        job.setPostTime(new Date().toString());
        job.setApplicants(new ArrayList<>());
        job.setBlackList(new ArrayList<>());

        Consumer<String> errors = PostAvailableJobHelper::errorEater;
        job.writeToDatabase(database,errors);
    }

    private Address locToCoordinates(String streetAdd, String city, String province) throws IOException {
        String strAddress = streetAdd + ", " + city + ", " + province + ", Canada";
        List<Address> address = geocoder.getFromLocationName(strAddress, 20);
        return address.get(0);
    }

    private double salaryStringToDouble(String strSalary){
        return Double.parseDouble(strSalary);
    }

    private double durationToDouble(String duration) {
        double doubleDuration = 0;

        if ("Under 8 Hours".equals(duration)) {
            doubleDuration = 8;
        } else if ("1 – 3 Days".equals(duration)) {
            doubleDuration = 24;
        } else if ("4 – 7 Days".equals(duration)) {
            doubleDuration = 56;
        } else if ("1 – 2 Weeks".equals(duration)) {
            doubleDuration = 112;
        } else if ("2 – 4 Weeks".equals(duration)) {
            doubleDuration = 240;
        } else if ("4+ Weeks".equals(duration)) {
            doubleDuration = 336;
        }

        return doubleDuration;
    }

    //fix this
    public static void errorEater(String error){
        String errorHolder = error;
    }
}
