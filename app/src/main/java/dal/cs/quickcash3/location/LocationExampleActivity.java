package dal.cs.quickcash3.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class LocationExampleActivity extends AppCompatPermissionActivity {
    private static final String LOG_TAG = LocationExampleActivity.class.getName();
    private final AtomicReference<Location> location = new AtomicReference<>();
    private final AtomicBoolean buttonWaiting = new AtomicBoolean(false);
    private TextView status;
    private TextView latTextbox;
    private TextView longTextbox;

    @SuppressLint("StringFormatTrivial") // There are two conflicting errors here so I chose to ignore one.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_example);

        LocationProvider locationProvider = new AndroidLocationProvider(this, 5000); // Update location every 5 seconds.

        status = findViewById(R.id.locationStatus);
        latTextbox = findViewById(R.id.latText);
        longTextbox = findViewById(R.id.longText);

        locationProvider.addLocationCallback(
            location -> {
                this.location.set(location);
                if (buttonWaiting.get() && location != null) {
                    showLocation(location);
                }
            },
            error -> status.setText(String.format("Error: %s", error)));

        Button detectButton = findViewById(R.id.detectButton);
        detectButton.setOnClickListener(view -> {
            try {
                Location currentLocation = location.get();
                if (currentLocation == null) {
                    buttonWaiting.set(true);
                }
                else {
                    showLocation(currentLocation);
                }
            } catch (SecurityException exception) {
                Log.w(LOG_TAG, Objects.requireNonNull(exception.getMessage()));
                status.setText(String.format("%s: Not Granted", getString(R.string.location_permission)));
                latTextbox.setText(String.format("%s: ERROR", getString(R.string.latitude)));
                longTextbox.setText(String.format("%s: ERROR", getString(R.string.longitude)));
            }
        });
    }

    private void showLocation(@NonNull Location location) {
        status.setText(String.format("%s: Granted", getString(R.string.location_permission)));
        latTextbox.setText(String.format("%s: %s", getString(R.string.latitude), location.getLatitude()));
        longTextbox.setText(String.format("%s: %s", getString(R.string.longitude), location.getLongitude()));
    }
}