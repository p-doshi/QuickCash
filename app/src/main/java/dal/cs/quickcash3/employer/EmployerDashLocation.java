package dal.cs.quickcash3.employer;

import static dal.cs.quickcash3.location.LocationHelper.coordinatesToAddress;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;
/**
 * Represents the location dashboard for employers within the application.
 * This class extends {@link AppCompatPermissionActivity} to handle permissions
 * and integrates with the device's location services to fetch and display the current location.
 */
public class EmployerDashLocation extends AppCompatPermissionActivity {
    private AndroidLocationProvider locationProvider;
    private TextView addressText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_dash_location);

        locationProvider = new AndroidLocationProvider(this, 1000);

        addressText = findViewById(R.id.addressText);
        Button detectLocationButton = findViewById(R.id.detectLocationButton);
        detectLocationButton.setOnClickListener(v -> detectLocation());
    }

    private void detectLocation() {
        locationProvider.fetchLocation(this::showAddress, this::showError);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    @SuppressLint("SetTextI18n")
    private void showAddress(@NonNull LatLng location) {
        coordinatesToAddress(
            this,
            location,
            address -> addressText.setText("Address: " + address),
            this::showError);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private void showError(@NonNull String error) {
        addressText.setText(error);
    }
}