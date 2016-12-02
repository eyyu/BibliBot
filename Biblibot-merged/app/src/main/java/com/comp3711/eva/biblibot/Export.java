package com.comp3711.eva.biblibot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Export extends AppCompatActivity {
    private final String TAG = Export.class.getName();

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

        TextView resultText = (TextView) findViewById(R.id.citation_details);
        final String citationDetails = (String) resultText.getText();

        // Function for export
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "to"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Bibliography from Biblibot");
                email.putExtra(Intent.EXTRA_TEXT, citationDetails);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Share Bibliography via: "));
            }
        });

    }

    private class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private String     ISBN;
        private String     results;
        private JSONObject jsonData;

        public String getResults() {
            return results;
        }
        public JSONObject getJsonData() {
            return jsonData;
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
                        jsonData = getJSON(results);
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

        // Outputs scanned result to TextView
        private JSONObject getJSON(String jsonResult) {
            JSONObject result;
            try {
                result = new JSONObject(jsonResult);
            } catch (JSONException ex) {
                Log.e(TAG, "Error parsing string " + ex);
                result = null;
            }

            return result;
        }

        private Citation createCitation(JSONObject data, String format) {
            Citation citation = new Citation();

            String[] citAuthors = null;
            String title        = null;
            String publisher    = null;
            String date         = null;

            try
            {
                //JSONArray authors = data.getJSONArray("authors");
                JSONArray items = data.getJSONArray("items");
                JSONObject obj = items.optJSONObject(0);
                JSONObject volumeInfo = obj.getJSONObject("volumeInfo");

                JSONArray authors = volumeInfo.getJSONArray("authors");
                citAuthors = new String[authors.length()];
                for (int j = 0; j < authors.length(); j++)
                {
                    citAuthors[j] = authors.getString(j);
                }

                title = volumeInfo.getString("title");
                publisher = volumeInfo.getString("publisher");
                date = volumeInfo.getString("publishedDate");
                String[] tmp;// = new String[citAuthors.length];//citAuthors[0].split(" ");
                String[] first = new String[citAuthors.length];
                String[] last = new String[citAuthors.length];

                for (int i = 0; i < citAuthors.length; i++) {
                    tmp = citAuthors[i].split(" ");
                    first[i] = tmp[0];
                    last[i] = tmp[tmp.length - 1];
                }

                citation.setfName(first);
                citation.setlName(last);
                citation.setTitle(title);
                citation.setPublisher(publisher);
                citation.setPubDate(date);
                citation.setType("BOOK");
                return citation;

            } catch (Exception e) {
                Log.e(TAG, "createCitation: error parsing JSONObject ", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String r) {
            String citation;
            Citation cite;
            TextView resultText = (TextView) findViewById(R.id.citation_details);

            cite = createCitation(jsonData, "MLA");
            if (cite != null) {
                citation = MLAFormat.bookFormat(cite.getfName(), cite.getlName(),
                        cite.getTitle(), cite.getPublisher(),
                        cite.getPubDate());
                resultText.setText(Html.fromHtml(citation));
                final DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                dbhelper.insertCitation(cite, cite.getfName(), cite.getlName());
                dbhelper.close();
            } else
                resultText.setText("ERROR: Book data could not be found");
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
