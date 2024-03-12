package dal.cs.quickcash3.location;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class LocationExampleActivity extends AppCompatPermissionActivity {
    private LocationProvider locationProvider;

    @SuppressLint("StringFormatTrivial") // There are two conflicting errors here so I chose to ignore one.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_example);

        locationProvider = new AndroidLocationProvider(this, 5000); // Update location every 5 seconds.

        TextView status = findViewById(R.id.locationStatus);
        TextView latTextbox = findViewById(R.id.latText);
        TextView longTextbox = findViewById(R.id.longText);

        Button detectButton = findViewById(R.id.detectButton);
        detectButton.setOnClickListener(view -> locationProvider.fetchLocation(
            location -> {
                status.setText(String.format("%s: Granted", getString(R.string.location_permission)));
                latTextbox.setText(String.format("%s: %s", getString(R.string.latitude), location.getLatitude()));
                longTextbox.setText(String.format("%s: %s", getString(R.string.longitude), location.getLongitude()));
            },
            error -> {
                status.setText(String.format("%s: Not Granted", getString(R.string.location_permission)));
                latTextbox.setText(String.format("%s: ERROR", getString(R.string.latitude)));
                longTextbox.setText(String.format("%s: ERROR", getString(R.string.longitude)));
            }));
    }
}