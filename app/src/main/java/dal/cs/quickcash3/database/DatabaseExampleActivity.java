package dal.cs.quickcash3.database;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class DatabaseExampleActivity extends AppCompatActivity {
    private static final String LOG_TAG = DatabaseExampleActivity.class.getName();
    private Database database;

    @SuppressLint("SetTextI18n") // There are two conflicting lint warnings, so I silenced one.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);
        initInterfaces();

        final String dbKey = "test/DatabaseExampleActivity";

        // Get the text view.
        TextView output = findViewById(R.id.dbOutput);

        Button writeButton = findViewById(R.id.writeButton);
        writeButton.setOnClickListener(v -> {
            String temp = RandomStringGenerator.generate(10);
            database.write(dbKey, temp,
                () -> output.setText(getString(R.string.db_write) + ": " + temp),
                error -> output.setText(getString(R.string.db_error_writing) + ": " + error));
        });

        Button readButton = findViewById(R.id.readButton);
        readButton.setOnClickListener(
            v -> database.read(dbKey, String.class,
                temp -> output.setText(getString(R.string.db_read) + ": " + temp),
                error -> output.setText(getString(R.string.db_error_reading) + ": " + error)
            ));
    }

    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.d(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase(this);
        }
    }

    public @NonNull Database getDatabase() {
        return database;
    }
}