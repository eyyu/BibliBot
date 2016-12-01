package com.comp3711.eva.biblibot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CitationView extends AppCompatActivity {
    private String projectName;
    private String[] citations;
    private DatabaseHelper databaseHelper;
    private String[] projectList = {};
//    private final String[] projectList = {"project 1", "project 2", "project 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation_view);

        ListView lv = (ListView) findViewById(R.id.list_view);
        registerForContextMenu(lv);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        projectList = databaseHelper.getAllProjects();

        Intent citationView = getIntent();
        projectName = citationView.getStringExtra("project");
        citations = null;
        citations = databaseHelper.getCitationsByProjectName(projectName);

        final ArrayList<String> citationNameList = new ArrayList<>();
        for(String c : citations){
            citationNameList.add(c);
        }

        // Binding resources Array to ListAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, citationNameList);
        lv.setAdapter(adapter);

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), CitationDetail.class);
                // pass clicked citation title
                i.putExtra("citation", citationNameList.get(position));
                startActivity(i);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(R.string.add_to_project);
            String[] menuItems = projectList;
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // get the info of the list clicked on
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        // get the id of current menu item clicked
        int menuItemIndex = item.getItemId();

        // project name that was clicked on
        String listItemName = projectList[info.position];

        // add to project



//        TextView text = (TextView)findViewById(R.id.clickeditem);
//        text.setText(String.format("Selected for item %s", listItemName));
        return true;
    }
}
