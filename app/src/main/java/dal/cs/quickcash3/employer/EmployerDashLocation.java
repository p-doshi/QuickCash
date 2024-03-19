package dal.cs.quickcash3.employer;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;
/**
 * Represents the location dashboard for employers within the application.
 * This class extends {@link AppCompatPermissionActivity} to handle permissions
 * and integrates with the device's location services to fetch and display the current location.
 */
public class EmployerDashLocation extends AppCompatPermissionActivity {
    private final AtomicReference<Location> currentLocation = new AtomicReference<>();
    private final AtomicBoolean buttonWaiting = new AtomicBoolean(false);
    private AndroidLocationProvider locationProvider;
    private TextView addressText;
    private Geocoder geocoder;
    private List<Address> addresses;
    private int callbackId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_dash_location);
        init();
    }

    @Override
    protected void onDestroy() {
        locationProvider.removeLocationCallback(callbackId);
        super.onDestroy();
    }

    /**
     * Initializes UI components and sets up the location provider and geocoder.
     * Also sets a click listener on the 'detect location' button to start location detection.
     */
    private void init() {
        Button detectLocationButton = findViewById(R.id.detectLocationButton);
        addressText = findViewById(R.id.addressText);
        locationProvider = new AndroidLocationProvider(this, 1000);
        callbackId = locationProvider.addLocationCallback(
                location -> {
                    this.currentLocation.set(location);
                    if (buttonWaiting.get() && location != null) {
                        updateAddressDisplay();
                    }
                },
                error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
        geocoder = new Geocoder(this, Locale.getDefault());

        detectLocationButton.setOnClickListener(v -> detectLocation());
    }
    /**
     * Initiates the location detection process. Uses the {@link AndroidLocationProvider} to fetch the current location
     * and updates the UI with the retrieved address. Shows a toast message in case of an error.
     */
    private void detectLocation() {
        if (currentLocation.get() == null) {
            buttonWaiting.set(true);
        }
        else {
            updateAddressDisplay();
        }
    }

    /**
     * Updates the address display TextView with the current address.
     * Converts the current location's latitude and longitude into a human-readable address using {@link Geocoder}.
     * Displays a message if the address is not found or an error occurs during the geocoding process.
     */
    @SuppressLint("SetTextI18n")
    private void updateAddressDisplay() {
        try {
            do {
                Location location = currentLocation.get();
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } while (addresses == null);
            runOnUiThread(() -> {
                if (!addresses.isEmpty()) {
                    String addressLine = addresses.get(0).getAddressLine(0);
                    addressText.setText("Address: " + addressLine);
                } else {
                    addressText.setText("Address not found");
                }
            });
        } catch (IOException e) {
            Log.e("EmployerDashLocation", "Geocoder failed", e);
        }
    }
}
