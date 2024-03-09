package dal.cs.quickcash3.database;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class DatabaseExampleActivity extends AppCompatActivity {

    private Database database;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_example);

        // The easiest way to run this example is to change the launch activity in the android manifest file.
        // Refer to this source for help: https://stackoverflow.com/questions/3631982/change-applications-starting-activity

        database = new MyFirebaseDatabase(this);
        String dbKey = "test";

        // Get the text view.
        TextView output = findViewById(R.id.dbOutput);

        Button sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> {
            String temp = RandomStringGenerator.generate(10);
            database.write(dbKey, temp,
                () -> output.setText("Sent: " + temp),
                error -> output.setText("Error sending: " + error));
        });

        Button recvBtn = findViewById(R.id.recvButton);
        recvBtn.setOnClickListener(
            v -> database.read(dbKey, String.class,
                temp -> output.setText("Received: " + temp),
                error -> output.setText("Error: " + error)
        ));
    }
}