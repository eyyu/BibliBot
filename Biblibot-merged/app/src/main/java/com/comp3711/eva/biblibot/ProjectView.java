package com.comp3711.eva.biblibot;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.database.DatabaseUtilsCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProjectView extends Activity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        try {
            databaseHelper = new DatabaseHelper(getApplicationContext());
        }catch(Exception e){
            Toast.makeText(this, "database failed", Toast.LENGTH_LONG).show();
        }

        //databaseHelper.checkIsSet();

        ListView lv = (ListView) findViewById(R.id.list_view);

        final String[] projectList = databaseHelper.getAllProjects();
//        final String[] projectList = {"project 1", "project 2"};

        // Binding resources Array to ListAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projectList);
        lv.setAdapter(adapter);

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), CitationView.class);
                // send project name
                i.putExtra("project", projectList[position]);
                startActivity(i);
            }
        });
    }

    public void addNewProject(View view){
        EditText newProjectName = (EditText) findViewById(R.id.newProjectName);
        String newProjName = newProjectName.getText().toString();

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertProject(newProjName);

        finish();
        startActivity(getIntent());
    }

}
