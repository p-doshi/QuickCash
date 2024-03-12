package dal.cs.quickCash3.database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

import dal.cs.quickCash3.R;
import dal.cs.quickCash3.util.RandomStringGenerator;

public class DatabaseExampleActivity extends AppCompatActivity {
    private static final String logTag = "DatabaseExample";
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);
        getImplementations();

        final String dbKey = "test";

        // Get the text view.
        TextView output = findViewById(R.id.dbOutput);

        Button writeButton = findViewById(R.id.writeButton);
        writeButton.setOnClickListener(v -> {
            String temp = RandomStringGenerator.generate(10);
            db.write(dbKey, temp,
                () -> output.setText(String.format("%s: %s", getString(R.string.db_write), temp)),
                error -> output.setText(String.format("%s: %s", getString(R.string.db_error_writing), error)));
        });

        Button readButton = findViewById(R.id.readButton);
        readButton.setOnClickListener(
            v -> db.read(dbKey, String.class,
                temp -> output.setText(String.format("%s: %s", getString(R.string.db_read), temp)),
                error -> output.setText(String.format("%s: %s", getString(R.string.db_error_reading), error))
            ));
    }

    private void getImplementations() {
        Set<String> categories = getIntent().getCategories();
        if (categories != null) {
            for (String category : categories) {
                if (category.equals(getString(R.string.MOCK_DATABASE))) {
                    db = new MockDatabase();
                    Log.d(logTag, "Using Mock Database");
                }
            }
        }

        if (db == null) {
            db = new MyFirebaseDatabase(this);
        }
    }

    public Database getDatabase() {
        return db;
    }
}