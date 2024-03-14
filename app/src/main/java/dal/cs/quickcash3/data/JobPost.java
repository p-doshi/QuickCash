package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;

public abstract class JobPost {
    private String title;
    private String employerId;
    private String description;
    private LatLng location;
    private String pay;

    /**
     * Write the job post to the database.
     *
     * @param database The database to write to.
     * @param errorFunction The error function that is called in case of an error.
     */
    public abstract void writeToDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction);

    public @Nullable String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public @Nullable String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(@NonNull String employerId) {
        this.employerId = employerId;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public @Nullable LatLng getLocation() {
        return location;
    }

    public void setLocation(@NonNull LatLng location) {
        this.location = location;
    }

    public @Nullable String getPay() {
        return pay;
    }

    public void setPay(@NonNull String pay) {
        this.pay = pay;
    }
}
