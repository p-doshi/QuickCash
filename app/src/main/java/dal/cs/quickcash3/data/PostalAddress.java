package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PostalAddress {
    public static final String CANADA = "Canada";
    private String street;
    private String city;
    private String division; // The generic name for province/territory/state.
    private String country;

    public @Nullable String getStreet() {
        return street;
    }

    public void setStreet(@NonNull String street) {
        this.street = street;
    }

    public @Nullable String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public @Nullable String getDivision() {
        return division;
    }

    public void setDivision(@NonNull String division) {
        this.division = division;
    }

    public @Nullable String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    @Override
    public @NonNull String toString() {
        return street + ", " + city + ", " + division + ", " + country;
    }

    public static @NonNull PostalAddress createCanadianAddress(
        @NonNull String street,
        @NonNull String city,
        @NonNull String province)
    {
        PostalAddress address = new PostalAddress();
        address.setStreet(street);
        address.setCity(city);
        address.setDivision(province);
        address.setCountry(CANADA);
        return address;
    }
}
