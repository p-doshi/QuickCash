package dal.cs.quickcash3.employer;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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


        job.setEmployer(RandomStringGenerator.generate(30)); //how to do this
        job.setPostTime(new Date().toString());
        job.setApplicants(new ArrayList<>());
        job.setBlackList(new ArrayList<>());

        //job.writeToDatabase();
    }

    private Address locToCoordinates(String streetAdd, String city, String province) throws IOException {
        String strAddress = streetAdd + ", " + city + ", " + province + ", Canada";
        List<Address> address = geocoder.getFromLocationName(strAddress, 20);
        return address.get(0);
    }
    private double getLat(Address address){

        return address.getLatitude();
    }
    private double getLong(Address address){

        return address.getLongitude();
    }

    private double salaryStringToDouble(String strSalary){
        return Double.parseDouble(strSalary);
    }

    private double durationToDouble(String duration){
        double doubleDuration = 0;

        if(duration.equals("Under 8 Hours")){
            doubleDuration = 8;
        } else if (duration.equals("1 – 3 Days")) {
            doubleDuration = 24;
        } else if (duration.equals("4 – 7 Days")) {
            doubleDuration = 56;
        } else if (duration.equals("1 – 2 Weeks")) {
            doubleDuration = 112;
        } else if (duration.equals("2 – 4 Weeks")) {
            doubleDuration = 240;
        } else if(duration.equals("4+ Weeks")){
            doubleDuration = 336;
        }

        return doubleDuration;
    }
}
