package dal.cs.quickcash3.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseOwner;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.location.LocationProviderOwner;

public class SearchFragment extends Fragment {
//    private final Database database;
//    private final LocationProvider locationProvider;

//    public SearchFragment(@NonNull Activity activity) {
//        DatabaseOwner databaseOwner = (DatabaseOwner)activity;
//        LocationProviderOwner locationProviderOwner = (LocationProviderOwner)activity;
//        this.database = databaseOwner.getDatabase();
//        this.locationProvider = locationProviderOwner.getLocationProvider();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
