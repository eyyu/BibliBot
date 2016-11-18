package com.comp3711.eva.biblibot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Export extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        final Button btn = (Button) findViewById(R.id.exportbtn);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Intent export = getIntent();
        String scanned = export.getStringExtra("scanned");
        String isbn = export.getStringExtra("isbn");

        RetrieveFeedTask feedTask = new RetrieveFeedTask();
        feedTask.setISBN(isbn);
        feedTask.execute();

//        TextView tx = (TextView) findViewById(R.id.citation_details);
//        tx.setText(scanned);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence exportOptions[] = new CharSequence[] {"EMail", "Save as file", "Bluetooth"};

                builder.setTitle("Share Bibliography via");

                builder.setItems(exportOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                    }
                });
                builder.show();
                }
        });

/*        CharSequence exportOptions[] = new CharSequence[] {"EMail", "Save as file", "Bluetooth"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share Bibliography via");
        builder.setItems(exportOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
            }
        });
        builder.show();
        */
    }

    private class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private String ISBN;
        private String results;

        public String getResults() {
            return results;
        }

        public void setISBN(String isbn) {
            ISBN = isbn;
            Log.d("ActivityMain", "ISBN Set: " + isbn);

        }

        private boolean checkISBNSet() {
            if (ISBN == null) {
                Log.e("ERROR OCCURRED!", "MUST SET AN ISBN");
                return false;
            } else {
                return true;
            }
        }

        @Override
        protected String doInBackground(Void... urls) {
            if (checkISBNSet()) {
                try {
                    URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(
                                        urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        results = stringBuilder.toString();
                        return results;
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    results = null;
                    return results;
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(String r) {
            TextView resultText = (TextView) findViewById(R.id.citation_details);
            resultText.setText(results);
        }
    }

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

            Intent export = new Intent(getApplicationContext(), Export.class);

            //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            String scanned = "Scanned: " + result.getContents();
            String isbn = result.getContents();

            export.putExtra("scanned", scanned);
            export.putExtra("isbn", isbn);
            startActivity(export);
            finish();
        }
    }

    public void generate(final View view) {
        Intent export = new Intent(getApplicationContext(), Export.class);
        startActivity(export);
        finish();
    }


    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
