package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.security.InvalidParameterException;

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

        Button sendBtn = findViewById(R.id.writeButton);
        sendBtn.setOnClickListener(v -> {
            String temp = RandomStringGenerator.generate(10);
            db.write(dbKey, temp,
                () -> output.setText(String.format("%s: %s", getString(R.string.db_write), temp)),
                error -> output.setText(String.format("%s: %s", getString(R.string.db_error_writing), error)));
        });

        Button recvBtn = findViewById(R.id.readButton);
        recvBtn.setOnClickListener(
            v -> db.read(dbKey, String.class,
                temp -> output.setText(String.format("%s: %s", getString(R.string.db_read), temp)),
                error -> output.setText(String.format("%s: %s", getString(R.string.db_error_reading), error))
            ));
    }

    private void getImplementations() {
        for (String category : getIntent().getCategories()) {
            if (category.startsWith("android.intent.category")) {
                //noinspection UnnecessaryContinue
                continue;
            }
            else if (category.equals(getString(R.string.MOCK_DATABASE))) {
                db = new MockDatabase();
                Log.d(logTag, "Using Mock Database");
            }
            else {
                throw new InvalidParameterException("Unrecognized category: " + category);
            }
        }

        if (db == null) {
            db = new MyFirebaseDatabase(this);
        }
    }
}