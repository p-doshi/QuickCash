package dal.cs.quickcash3.location;

import androidx.annotation.NonNull;

public interface LocationProviderOwner {
    /**
     * Get the location provider from its owner.
     *
     * @return The location provider.
     */
    @NonNull LocationProvider getLocationProvider();
}
