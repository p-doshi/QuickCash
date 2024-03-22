package dal.cs.quickcash3.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder.GeocodeListener;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("PMD.LawOfDemeter") // This is proper usage of Build.VERSION_CODES.
@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
class MyNewGeocodeListener extends MyGeocodeListener implements GeocodeListener {
    public MyNewGeocodeListener(
        @NonNull Context context,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super(context, locationFunction, errorFunction);
    }

    @Override
    public void onGeocode(@Nullable List<Address> addresses) {
        super.onGeocode(addresses);
    }

    @Override
    public void onError(@Nullable String errorMessage) {
        super.onError(errorMessage);
    }
}
