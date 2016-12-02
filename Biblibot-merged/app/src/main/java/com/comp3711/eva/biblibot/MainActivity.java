package com.comp3711.eva.biblibot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.comp3711.eva.biblibot.R.id.citationStyle;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation);

        dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.checkIsSet();
    }

    // start scan
    public void scan(final View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result.getContents() == null)
        {
            Log.d("BarcodeScanner", "cancelled scan");
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);

            Intent export = new Intent(getApplicationContext(), ScanResultActivity.class);
            Spinner citationStyle = (Spinner) findViewById(R.id.citationStyle);
            int style = citationStyle.getSelectedItemPosition();
            export.putExtra("style", style);

            //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            String scanned = "Scanned: " + result.getContents();
            String isbn = result.getContents();

            export.putExtra("scanned", scanned);
            export.putExtra("isbn", isbn);
            startActivity(export);
        }
    }

    public void generate(final View view) {
        Intent export = new Intent(getApplicationContext(), ScanResultActivity.class);

        // indicate which style to use for citation
        Spinner citationStyle = (Spinner) findViewById(R.id.citationStyle);
        int style = citationStyle.getSelectedItemPosition();
        export.putExtra("style", style);
        startActivity(export);
    }

    // login page
    public void login(final View view) {
        Intent login = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(login);
    }

    // add and view projects
    public void projectView(final View view) {
        Intent projects = new Intent(getApplicationContext(), ProjectView.class);
        startActivity(projects);
    }
}
