package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DatabaseExampleActivity extends AppCompatActivity {
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);

        if (getIntent().hasCategory(getString(R.string.MOCK_DATABASE))) {
            db = new MockDatabase();
        }
        else {
            db = new MyFirebaseDatabase(this);
        }

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
    
    public Database getDatabase() {
        return db;
    }
}