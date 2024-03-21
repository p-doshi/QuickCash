package dal.cs.quickcash3.location;

import android.location.Address;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.function.Consumer;

class MyGeocodeListener extends LocationReceiver {
    public MyGeocodeListener(
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super(locationFunction, errorFunction);
    }

    public void onGeocode(@Nullable List<Address> addresses) {
        final LatLng location;

        if (addresses != null && !addresses.isEmpty()) {
            Address address1 = addresses.get(0);
            location = new LatLng(address1.getLatitude(), address1.getLongitude());
            receiveLocation(location);
        } else {
            receiveError("Could not get the location from the geocoder");
        }
    }

    public void onError(@Nullable String errorMessage) {
        if (errorMessage == null) {
            receiveError("Geocoder error with null message");
        }
        else {
            receiveError(errorMessage);
        }
    }
}
