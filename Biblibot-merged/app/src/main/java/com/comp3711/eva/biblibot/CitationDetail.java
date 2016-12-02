package com.comp3711.eva.biblibot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class CitationDetail extends AppCompatActivity {
    private String citationTitle;
    private DatabaseHelper databaseHelper;
    private Citation cite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation_detail);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Intent citationDetail = getIntent();
        citationTitle = citationDetail.getStringExtra("citation");

        cite = databaseHelper.getCitationByTitle(citationTitle);


        // Display citation
        TextView tv = (TextView) findViewById(R.id.citation_detail);
        tv.setText( Html.fromHtml(
                    MLAFormat.bookFormat(
                            cite.getfName(),
                            cite.getlName(),
                            cite.getTitle(),
                            cite.getPublisher(),
                            cite.getPubDate())
                            ));

    }
}
