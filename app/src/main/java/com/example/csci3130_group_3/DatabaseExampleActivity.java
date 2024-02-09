package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class DatabaseExampleActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);

        // The easiest way to run this example is to change the launch activity in the android manifest file.
        // Refer to this source for help: https://stackoverflow.com/questions/3631982/change-applications-starting-activity

        db = new MyFirebaseDatabase(this);
        String dbKey = "test";

        // Get the text view.
        TextView output = findViewById(R.id.dbOutput);

        Button sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> {
            String temp = RandomStringGenerator.generate(10);
            db.write(dbKey, temp,
                () -> output.setText("Sent: " + temp),
                error -> output.setText("Error sending: " + error));
        });

        Button recvBtn = findViewById(R.id.recvButton);
        recvBtn.setOnClickListener(
            v -> db.read(dbKey, String.class,
                temp -> output.setText("Received: " + temp),
                error -> output.setText("Error: " + error)
        ));
    }
}