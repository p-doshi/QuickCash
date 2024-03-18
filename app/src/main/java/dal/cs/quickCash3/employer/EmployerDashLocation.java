package dal.cs.quickcash3.employer;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.location.AndroidLocationProvider;

public class EmployerDashLocation extends AppCompatActivity {

    Button detectLocationButton;
    AndroidLocationProvider locationProvider;
    Location currentLocation;

    TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_automatically_location);
        init();
    }

    private void init(){
        detectLocationButton = findViewById(R.id.detectLocationButton);
        addressText =findViewById(R.id.addressText);
        locationProvider = new AndroidLocationProvider(this, this);
        detectLocationButton.setOnClickListener(v->detectLocation());}

    private void detectLocation(){
        if(!locationProvider.checkLocationPermissionsEnabled()){
            Toast.makeText(this, "Location permission is required to use this feature", Toast.LENGTH_SHORT).show();
        }
        currentLocation = locationProvider.getCurrentLocation();
        addressText.setText(currentLocation.toString());
    }
}