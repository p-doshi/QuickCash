package dal.cs.quickcash3.database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class DatabaseExampleActivity extends AppCompatActivity {
    private static final String LOG_TAG = "DatabaseExample";
    private Database database;

    @SuppressWarnings("PMD.AvoidDuplicateLiterals") // This would reduce clarity here.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);
        initInterfaces();

        final String dbKey = "test";

        // Get the text view.
        TextView output = findViewById(R.id.dbOutput);

        Button writeButton = findViewById(R.id.writeButton);
        writeButton.setOnClickListener(v -> {
            String temp = RandomStringGenerator.generate(10);
            database.write(dbKey, temp,
                () -> output.setText(String.format("%s: %s", getString(R.string.db_write), temp)),
                error -> output.setText(String.format("%s: %s", getString(R.string.db_error_writing), error)));
        });

        Button readButton = findViewById(R.id.readButton);
        readButton.setOnClickListener(
            v -> database.read(dbKey, String.class,
                temp -> output.setText(String.format("%s: %s", getString(R.string.db_read), temp)),
                error -> output.setText(String.format("%s: %s", getString(R.string.db_error_reading), error))
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

    public Database getDatabase() {
        return database;
    }
}