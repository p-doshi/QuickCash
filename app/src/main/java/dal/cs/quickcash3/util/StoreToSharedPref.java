package dal.cs.quickcash3.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import dal.cs.quickcash3.R;

/**
 * This class is a helper class to allow user to write and read from SharedPreferences object.
 * When worker set their job preferences, this class will be invoke to store their
 * preferences locally.
 *
 */
public class StoreToSharedPref {
    private final Context context;

    /**
     * StoreToSharedPref class constructor.
     * @param context get the context from activity class
     */
    public StoreToSharedPref(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Method used to write the salary range preference to SharedPreferences object.
     * @param salaryRange the range of salary that worker wants to save as their preference
     */
    public void storeSalaryPrefToSharedPref(@NonNull Range<Double> salaryRange) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(context.getString(R.string.salary_max), salaryRange.getEnd().floatValue());
        editor.putFloat(context.getString(R.string.salary_min), salaryRange.getStart().floatValue());
        editor.apply();
    }

    /**
     * Method used to write the duration range preference to SharedPreferences object.
     * @param timeRange the range of time that worker wants to save as their preference
     */
    public void storeTimePrefToSharedPref(@NonNull Range<Double> timeRange) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(context.getString(R.string.time_max), timeRange.getEnd().floatValue());
        editor.putFloat(context.getString(R.string.time_min), timeRange.getStart().floatValue());
        editor.apply();
    }

    /**
     * Method used to clear salary preference when user choose
     * to disable the salary range preference.
     */
    public void clearSalaryPref() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getString(R.string.salary_max));
        editor.remove(context.getString(R.string.salary_min));
        editor.apply();
    }

    /**
     * Method used to clear duration preference when user choose
     * to disable the duration range preference.
     */
    public void clearTimePref() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getString(R.string.time_max));
        editor.remove(context.getString(R.string.time_min));
        editor.apply();
    }
}
