package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import dal.cs.quickcash3.database.DatabaseObject;
import dal.cs.quickcash3.util.Copyable;

public abstract class JobPost implements DatabaseObject {
    protected static final int HASH_SIZE = 20;
    private String title;
    private String employer;
    private String description;
    private double latitude;
    private double longitude;
    private double salary;

    public @Nullable String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public @Nullable String getEmployer() {
        return employer;
    }

    public void setEmployer(@NonNull String employer) {
        this.employer = employer;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public @NonNull String toString() {
        return
            "\ntitle='" + title + '\'' +
            "\nemployer='" + employer + '\'' +
            "\ndescription='" + description + '\'' +
            "\nlatitude=" + latitude +
            "\nlongitude=" + longitude +
            "\nsalary=" + salary;
    }

    protected void copyFrom(@NonNull JobPost other) {
        title = other.title;
        employer = other.employer;
        description = other.description;
        latitude = other.latitude;
        longitude = other.longitude;
        salary = other.salary;
    }
}
