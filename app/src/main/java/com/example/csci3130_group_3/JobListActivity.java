package com.example.csci3130_group_3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JobListActivity extends AppCompatActivity {

    private String jobList[]={"Example1", "Example 2", "Example 3", "Example 4"};
//    @Override
//    protected void onCreate(Bundle savedInstances){
//        super.onCreate(savedInstances);
//        setContentView(R.layout.job_search_page);
//
//        ListView searchResults = findViewById(R.id.jobList);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(JobListActivity.this,
//                android.R.layout.simple_list_item_1, jobList);
//
//        searchResults.setAdapter(arrayAdapter);
//        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//                String item = (String) adapterView.getItemAtPosition(i);
//                Toast.makeText(JobListActivity.this,"Item selected by"+item,Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
