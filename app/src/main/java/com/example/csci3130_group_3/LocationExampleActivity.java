package com.example.csci3130_group_3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LocationExampleActivity extends AppCompatActivity {
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_example);

        locationProvider = new AndroidLocationProvider(this);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationProvider.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}