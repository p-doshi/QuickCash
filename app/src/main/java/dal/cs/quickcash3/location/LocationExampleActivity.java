package dal.cs.quickcash3.location;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class LocationExampleActivity extends AppCompatPermissionActivity {
    private static final String LOG_TAG = LocationExampleActivity.class.getSimpleName();
    private TextView status;
    private TextView latText;
    private TextView longText;
    private LocationProvider locationProvider;

    @SuppressLint("StringFormatTrivial") // There are two conflicting errors here so I chose to ignore one.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_example);

        latText = findViewById(R.id.latText);
        longText = findViewById(R.id.longText);
        status = findViewById(R.id.locationStatus);

        locationProvider = new AndroidLocationProvider(this, 5000); // Update location every 5 seconds.

        Button detectButton = findViewById(R.id.detectButton);
        detectButton.setOnClickListener(view ->
            locationProvider.fetchLocation(this::showLocation, this::showError));
    }

    @SuppressWarnings("PMD.LawOfDemeter") // There is not other way to do this.
    @SuppressLint("SetTextI18n") // There are two conflicting lint warnings, so I silenced one.
    private void showLocation(@NonNull LatLng location) {
        status.setText(getString(R.string.location_permission) + ": Granted");
        latText.setText(getString(R.string.latitude) + ": " + location.latitude);
        longText.setText(getString(R.string.longitude) + ": " + location.longitude);
    }

    @SuppressLint("SetTextI18n") // There are two conflicting lint warnings, so I silenced one.
    private void showError(@NonNull String error) {
        Log.w(LOG_TAG, error);
        status.setText(getString(R.string.location_permission) + ": Not Granted");
        latText.setText(getString(R.string.latitude) + ": ERROR");
        longText.setText(getString(R.string.longitude) + ": ERROR");
    }
}