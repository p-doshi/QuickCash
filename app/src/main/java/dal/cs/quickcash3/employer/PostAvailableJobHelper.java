package dal.cs.quickcash3.employer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.util.RandomStringGenerator;

public final class PostAvailableJobHelper {
    private Database database;
    public void createAvailableJob(Map<String, String> fields) throws IOException {
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
        Context context = null;
        Geocoder geocoder = new Geocoder(context);
        String strAddress = streetAdd + ", " + city + ", " + province + ", Canada";
        List<Address> address = geocoder.getFromLocationName(strAddress, 20);
        return address.get(0);
    }

    private double salaryStringToDouble(String strSalary){
        return Double.parseDouble(strSalary);
    }

    private double durationToDouble(String duration) {
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

    //fix this
    @SuppressLint("SystemPrintln") // I don't have a way of handling this yet so just go with it
    public static void errorEater(String error){
        System.out.println(error);
    }
}
