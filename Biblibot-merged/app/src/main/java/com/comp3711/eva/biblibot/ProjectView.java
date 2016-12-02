package com.comp3711.eva.biblibot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class ProjectView extends Activity {

    private DatabaseHelper databaseHelper;
    private String projToExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        try {
            databaseHelper = new DatabaseHelper(getApplicationContext());
        }catch(Exception e){
            Toast.makeText(this, "database failed", Toast.LENGTH_LONG).show();
        }

        ListView lv = (ListView) findViewById(R.id.list_view);
        registerForContextMenu(lv);

        final String[] projectList = databaseHelper.getAllProjects();

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

    // add new project
    public void addNewProject(View view){
        EditText newProjectName = (EditText) findViewById(R.id.newProjectName);
        String newProjName = newProjectName.getText().toString();

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertProject(newProjName);

        finish();
        startActivity(getIntent());
    }

    // option for long click context menu: export
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String[] projectList = databaseHelper.getAllProjects();
        projToExport = projectList[info.position];

        menu.setHeaderTitle(R.string.export);
        menu.add(Menu.NONE, 0, 0, "Export in MLA");
        menu.add(Menu.NONE, 1, 1, "Export in APA");
        menu.add(Menu.NONE, 2, 2, "Export in Chicago");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String [] allCitationTitles = {};
        String citation = "";
        int styleSelected;

        // get the info of the list clicked on
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        styleSelected = item.getOrder();

        // get citation titles of the selected project
        databaseHelper = new DatabaseHelper(getApplicationContext());
        allCitationTitles = databaseHelper.getCitationsByProjectName(projToExport);

        switch (styleSelected){
            case 0:
                // concatonate citations into one string
                for(String title : allCitationTitles){
                    Citation c = databaseHelper.getCitationByTitle(title);
                    citation += "<br>";
                    citation += (MLAFormat.bookFormat(c.getfName(), c.getlName(),
                            c.getTitle(), c.getPublisher(),
                            c.getPubDate()));
                    citation += "<br>";
                }
                break;
            case 1:
                // concatonate citations into one string
                for(String title : allCitationTitles){
                    Citation c = databaseHelper.getCitationByTitle(title);
                    citation += "<br>";
                    citation += (APAFormat.bookFormat(c.getlName(), c.getfName(),
                            c.getPubYear(), c.getTitle(), c.getSubtitle(),
                            c.getLocation(), c.getPublisher()));
                    citation += "<br>";
                }
                break;
            case 2:
                // concatonate citations into one string
                for(String title : allCitationTitles){
                    Citation c = databaseHelper.getCitationByTitle(title);
                    citation += "<br>";
                    citation += (ChicagoFormat.bookFormat(c.getlName(), c.getfName(),
                            c.getTitle(), c.getLocation(), c.getPublisher(), c.getPubYear()));
                    citation += "<br>";
                }
                break;
        }


        // export options (with html if available)
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/plain");
        email.putExtra(Intent.EXTRA_SUBJECT, "Bibliography from Biblibot");
        email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(citation));
        email.putExtra(Intent.EXTRA_HTML_TEXT, citation);
        startActivity(Intent.createChooser(email, "Share Bibliography via: "));

        return true;
    }
}
