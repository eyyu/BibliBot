package com.comp3711.eva.biblibot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CitationDetail extends AppCompatActivity {
    private String citationTitle;
    private DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
    private Citation citation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation_detail);

        Intent citationDetail = getIntent();
        citationTitle = citationDetail.getStringExtra("citation");

        citation = databaseHelper.getCitationByTitle(citationTitle);


        // Display citation
        TextView tv = (TextView) findViewById(R.id.citation_detail);
        tv.setText("Citation details");

    }
}
