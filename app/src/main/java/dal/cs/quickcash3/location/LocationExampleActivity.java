package dal.cs.quickcash3.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class LocationExampleActivity extends AppCompatPermissionActivity {
    private static final String LOG_TAG = LocationExampleActivity.class.getName();
    private final AtomicReference<Location> location = new AtomicReference<>();
    private final AtomicBoolean buttonWaiting = new AtomicBoolean(false);
    private TextView status;
    private TextView latText;
    private TextView longText;

    @SuppressLint("StringFormatTrivial") // There are two conflicting errors here so I chose to ignore one.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_example);

        latText = findViewById(R.id.latText);
        longText = findViewById(R.id.longText);
        status = findViewById(R.id.locationStatus);

        LocationProvider locationProvider = new AndroidLocationProvider(this, 5000); // Update location every 5 seconds.
        locationProvider.addLocationCallback(
            location -> {
                this.location.set(location);
                if (buttonWaiting.get() && location != null) {
                    showLocation(location);
                }
            },
            this::showError);

        Button detectButton = findViewById(R.id.detectButton);
        detectButton.setOnClickListener(view -> {
            Location currentLocation = location.get();
            if (currentLocation == null) {
                buttonWaiting.set(true);
            }
            else {
                showLocation(currentLocation);
            }
        });
    }

    @SuppressLint("SetTextI18n") // There are two conflicting lint warnings, so I silenced one.
    private void showLocation(@NonNull Location location) {
        status.setText(getString(R.string.location_permission) + ": Granted");
        latText.setText(getString(R.string.latitude) + ": " + location.getLatitude());
        longText.setText(getString(R.string.longitude) + ": " + location.getLongitude());
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    @SuppressLint("SetTextI18n") // There are two conflicting lint warnings, so I silenced one.
    private void showError(@NonNull String error) {
        Log.w(LOG_TAG, error);
        status.setText(getString(R.string.location_permission) + ": Not Granted");
        latText.setText(getString(R.string.latitude) + ": ERROR");
        longText.setText(getString(R.string.longitude) + ": ERROR");
    }
}