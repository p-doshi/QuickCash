package dal.cs.quickcash3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.location.LocationProvider;

public class SearchFragment extends Fragment {
    private final Database database;
    private final LocationProvider locationProvider;

    public SearchFragment(Database database, LocationProvider locationProvider) {
        this.database = database;
        this.locationProvider = locationProvider;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
