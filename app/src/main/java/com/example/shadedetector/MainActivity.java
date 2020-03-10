package com.example.shadedetector;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText mPhrase;
    private ProgressBar mProgressBar;
    private TextView mResult;
    private TextView mResult2;
    private TextView mResult3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        mPhrase = (EditText) findViewById(R.id.phrase_edit_text);
        mResult = (TextView) findViewById(R.id.result_text);
        mResult2 = (TextView) findViewById(R.id.result_text2);
        mResult3 = (TextView) findViewById(R.id.result_text3);

    }

    public void onClick(View view) {

        Uri builtUri2 = Uri.parse("https://api.meaningcloud.com/sentiment-2.1").buildUpon()
                .appendQueryParameter("key","5cefecfee765dde6c9c943db4c891c88")
                .appendQueryParameter("txt", mPhrase.getText().toString())
                .appendQueryParameter("lang", "en")
                .build();
        mProgressBar.setVisibility(View.VISIBLE);
        new FindSentimentTask().execute(builtUri2.toString());

    }

    public static String getResponseFromHttpUrl(String url) throws IOException {

        URL theURL= new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) theURL.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    private class FindSentimentTask extends AsyncTask<String , Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String toReturn = "DID NOT WORK";
            try {
                toReturn = getResponseFromHttpUrl(strings[0]);

            } catch (Exception e) {
                Log.d("ErrorInApp", "exception on get Response from HTTP call" + e.getMessage());
            }
            return toReturn;
        }
        protected void  onPostExecute(String jsonToParse)
        {
            mProgressBar.setVisibility(View.GONE);
            // mResult.setText(jsonToParse);

            String score = "";
            String confidence = "";
            String irony = "";

            try{
                JSONObject sentimentJSON = new JSONObject(jsonToParse);
                score = sentimentJSON.get("score_tag").toString();
                confidence = sentimentJSON.get("confidence").toString();
                irony = sentimentJSON.get("irony").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mResult.setText(score);
            mResult2.setText(confidence);
            mResult3.setText(irony);
        }
    }
}
