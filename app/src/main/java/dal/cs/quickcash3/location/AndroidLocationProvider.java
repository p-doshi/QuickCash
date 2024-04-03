package dal.cs.quickcash3.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;
import dal.cs.quickcash3.permission.PermissionRequestCode;
import dal.cs.quickcash3.permission.PermissionResult;

public class AndroidLocationProvider implements LocationProvider {
    protected final FusedLocationProviderClient locationProviderClient;
    protected final Activity activity;
    private final long updateFrequencyMillis;
    protected final AtomicReference<LatLng> lastLocation = new AtomicReference<>();
    private final AtomicBoolean hasLocationUpdates = new AtomicBoolean(false);
    private final AtomicBoolean deniedPermission = new AtomicBoolean(false);
    private final AtomicReference<Map<Integer, LocationReceiver>> locationReceivers = new AtomicReference<>(new TreeMap<>());
    private final AtomicReference<List<LocationReceiver>> oneTimeReceivers = new AtomicReference<>(new ArrayList<>());
    private int nextReceiverId;

    public AndroidLocationProvider(@NonNull AppCompatPermissionActivity activity, long updateFrequencyMillis) {
        this.activity = activity;
        this.updateFrequencyMillis = updateFrequencyMillis;
        activity.registerPermissionHandler(this::onRequestPermissionsResult);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        startLocationUpdates();
    }

    private boolean missingPermissions() {
        return activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private void receiveLocation(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lastLocation.set(latLng);

        for (LocationReceiver receiver : locationReceivers.get().values()) {
            receiver.receiveLocation(latLng);
        }

        List<LocationReceiver> receivers = oneTimeReceivers.getAndSet(new ArrayList<>());
        for (LocationReceiver receiver : receivers) {
            receiver.receiveLocation(latLng);
        }
    }

    private void receiveError(@NonNull String error) {
        for (LocationReceiver receiver : locationReceivers.get().values()) {
            receiver.receiveError(error);
        }

        List<LocationReceiver> receivers = oneTimeReceivers.getAndSet(new ArrayList<>());
        for (LocationReceiver receiver : receivers) {
            receiver.receiveError(error);
        }
    }

    @SuppressLint("MissingPermission") // Using a helper function instead.
    private void startLocationUpdates() {
        if (missingPermissions()) {
            // Do not ask more than once.
            if (!deniedPermission.get()) {
                activity.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PermissionRequestCode.LOCATION.getCode());
            }
            return;
        }

        LocationRequest locationRequest =
            new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, updateFrequencyMillis)
                .build();

        LocationCallback locationCallback = new MyLocationCallback(
            locationProviderClient,
            this::receiveLocation);

        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            activity.getMainLooper());
        hasLocationUpdates.set(true);

        // Try to use the last known location for now.
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                return;
            }
            receiveLocation(location);
        });
    }

    @Override
    public int addLocationCallback(
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        if (missingPermissions() && deniedPermission.get()) {
            errorFunction.accept(activity.getString(R.string.error_location_permission));
            return -1;
        }

        // In case the user re-enabled permissions.
        if (!hasLocationUpdates.get()) {
            startLocationUpdates();
        }

        // This receiver may never be run. But it is important that we track all of them in case
        // permissions are re-enabled.
        LocationReceiver receiver = new LocationReceiver(locationFunction, errorFunction);
        int receiverId = nextReceiverId++;
        locationReceivers.get().put(receiverId, receiver);

        return receiverId;
    }

    @Override
    public void removeLocationCallback(int callbackId) {
        locationReceivers.get().remove(callbackId);
    }

    @Override
    public void fetchLocation(@NonNull Consumer<LatLng> locationFunction, @NonNull Consumer<String> errorFunction) {
        if (missingPermissions() && deniedPermission.get()) {
            errorFunction.accept(activity.getString(R.string.error_location_permission));
            return;
        }

        if (!hasLocationUpdates.get()) {
            startLocationUpdates();
        }

        LatLng location = lastLocation.get();
        if (location != null) {
            locationFunction.accept(location);
        }
        else {
            LocationReceiver receiver = new LocationReceiver(locationFunction, errorFunction);
            oneTimeReceivers.get().add(receiver);
        }
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is very much used.
    private void onRequestPermissionsResult(@NonNull PermissionResult result) {
        if (result.isMatchingCode(PermissionRequestCode.LOCATION) &&
            result.containsPermission(Manifest.permission.ACCESS_FINE_LOCATION))
        {
            if (result.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                startLocationUpdates();
            }
            else {
                deniedPermission.set(true);
                receiveError(activity.getString(R.string.error_location_permission));
            }
        }
    }
}
