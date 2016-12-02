package com.comp3711.eva.biblibot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

public class ScanResultActivity extends AppCompatActivity {
    private final String TAG = ScanResultActivity.class.getName();
    private static String citation = "";
    private static int style;
    private static Citation cite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        final Button btn = (Button) findViewById(R.id.exportbtn);

        Intent export = getIntent();
        style = export.getIntExtra("style", 0);
        String isbn = export.getStringExtra("isbn");

        RetrieveFeedTask feedTask = new RetrieveFeedTask();
        feedTask.setISBN(isbn);
        feedTask.execute();

//        TextView resultText = (TextView) findViewById(R.id.result_text);
//        final String citationDetails = (String) resultText.getText();

        Spinner citationStyle = (Spinner) findViewById(R.id.citation_type);
        citationStyle.setSelection(style);
        citationStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // regenerate citation according to new style selected if necessary
                TextView resultText = (TextView) findViewById(R.id.result_text);
                Spinner citationStyle = (Spinner) findViewById(R.id.citation_type);
                int styleNew = citationStyle.getSelectedItemPosition();
                if (cite != null) {
                    switch (styleNew){
                        case 0:
                            citation = "<br>";
                            citation += MLAFormat.bookFormat(cite.getfName(), cite.getlName(),
                                    cite.getTitle(), cite.getPublisher(),
                                    cite.getPubDate());
                            citation += "<br>";
                            break;
                        case 1:
                            citation = "<br>";
                            citation += APAFormat.bookFormat(cite.getlName(), cite.getfName(),
                                    cite.getPubYear(), cite.getTitle(), cite.getSubtitle(),
                                    cite.getLocation(), cite.getPublisher());
                            citation += "<br>";
                            break;
                        case 2:
                            citation = "<br>";
                            citation += ChicagoFormat.bookFormat(cite.getlName(), cite.getfName(),
                                    cite.getTitle(), cite.getLocation(), cite.getPublisher(), cite.getPubYear());
                            citation += "<br>";
                            break;
                    }

                    resultText.setText(Html.fromHtml(citation));
                } else {
                    resultText.setText("ERROR: Book data could not be found");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Function for export
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // export options (with html if available)
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_SUBJECT, "Bibliography from Biblibot");
                email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(citation));
                email.putExtra(Intent.EXTRA_HTML_TEXT, citation);
                startActivity(Intent.createChooser(email, "Share Bibliography via: "));
            }
        });
    }

    // build
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
            String year         = null;
            String subtitle     = null;
            String location     = null;

            try
            {
                //JSONArray authors = data.getJSONArray("authors");
                JSONArray items = data.getJSONArray("items");
                JSONObject obj = items.optJSONObject(0);
                JSONObject volumeInfo = obj.getJSONObject("volumeInfo");
                JSONObject accessInfo = obj.getJSONObject("accessInfo");

                JSONArray authors = volumeInfo.getJSONArray("authors");
                citAuthors = new String[authors.length()];
                for (int j = 0; j < authors.length(); j++)
                {
                    citAuthors[j] = authors.getString(j);
                }

                title = volumeInfo.getString("title");
                publisher = volumeInfo.getString("publisher");
                date = volumeInfo.getString("publishedDate");
                if(date != null) {year = date.substring(0,4);}
                if(subtitle != null) {subtitle = volumeInfo.getString("subtitle");};
                if(location != null) {location = accessInfo.getString("country");} else { location = "N/A";}
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
                citation.setPubYear(year);
                citation.setType("BOOK");
                citation.setSubtitle(subtitle);
                citation.setLocation(location);
                return citation;

            } catch (Exception e) {
                Log.e(TAG, "createCitation: error parsing JSONObject ", e);
            }

            return null;
        }

        // set citation string to textview
        @Override
        protected void onPostExecute(String r) {

            TextView resultText = (TextView) findViewById(R.id.result_text);

            cite = createCitation(jsonData, "MLA");

            if (cite != null) {
                switch (style){
                    case 0:
                        citation = "<br>";
                        citation += MLAFormat.bookFormat(cite.getfName(), cite.getlName(),
                                cite.getTitle(), cite.getPublisher(),
                                cite.getPubDate());
                        citation += "<br>";
                        break;
                    case 1:
                        citation = "<br>";
                        citation += APAFormat.bookFormat(cite.getlName(), cite.getfName(),
                                cite.getPubYear(), cite.getTitle(), cite.getSubtitle(),
                                cite.getLocation(), cite.getPublisher());
                        citation += "<br>";
                        break;
                    case 2:
                        citation = "<br>";
                        citation += ChicagoFormat.bookFormat(cite.getlName(), cite.getfName(),
                                cite.getTitle(), cite.getLocation(), cite.getPublisher(), cite.getPubYear());
                        citation += "<br>";
                        break;
                }

                resultText.setText(Html.fromHtml(citation));
                final DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                dbhelper.insertCitation(cite, cite.getfName(), cite.getlName());
                dbhelper.close();
            } else
                resultText.setText("ERROR: Book data could not be found");
        }
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
            Spinner citationStyle = (Spinner) findViewById(R.id.citation_type);
            int style = citationStyle.getSelectedItemPosition();
            export.putExtra("style", style);

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
        Intent export = new Intent(getApplicationContext(), ScanResultActivity.class);
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
