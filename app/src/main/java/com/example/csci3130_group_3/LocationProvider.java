package com.example.csci3130_group_3;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface LocationProvider {
    /**
     * Get the location. If successful, the locationFunction will be called. Otherwise, the
     * errorFunction will be called.
     * @param locationFunction The function that receives the location iff accessible. The location will never be null.
     * @param errorFunction The function that receives errors iff they occur.
     */
    void fetchLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction);

    /**
     * This MUST be called from the activity that uses this or this interface may not work!
     * @param requestCode The request code we used.
     * @param permissions The permissions we requested.
     * @param grantResults The permissions we were granted.
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
