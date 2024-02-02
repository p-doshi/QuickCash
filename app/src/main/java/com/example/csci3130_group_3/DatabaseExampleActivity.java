package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class DatabaseExampleActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);

        // The easiest way to run this example is to change the launch activity in the android manifest file.
        // Refer to this source for help: https://stackoverflow.com/questions/3631982/change-applications-starting-activity

        db = new MyFirebaseDatabase(this);
        Employer emp = new Employer("Parth", "alkjo139ksdf09812n");

        Button sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> {
            db.write("test", emp);
            Log.d("send", "sent");
        });

        Button recvBtn = findViewById(R.id.recvButton);
        recvBtn.setOnClickListener(v -> {
            db.read(
                "test",
                (String message) -> Log.d("recv", message),
                error -> Log.d("recv", error)
            );
        });
    }
}