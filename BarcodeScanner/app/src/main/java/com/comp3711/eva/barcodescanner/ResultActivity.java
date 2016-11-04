package com.comp3711.eva.barcodescanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {
    private String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final Intent intent = getIntent();
        isbn = intent.getStringExtra("ISBN_INTENT");
        setResultText();
    }

    private void setResultText() {
        RetrieveFeedTask feedTask = new RetrieveFeedTask();
        feedTask.setISBN(this.isbn);
        feedTask.execute();
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
            TextView resultText = (TextView) findViewById(R.id.resultText);
            resultText.setText(results);
        }
    }
}

