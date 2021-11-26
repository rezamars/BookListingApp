package com.example.android.booklistingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //private static String APIQuery = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    private String searchTextString = "YYYYY";
    private String APIQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void startSearch(View textView){

        TextView searchTextView = findViewById(R.id.searchText);
        searchTextString = searchTextView.getText().toString();

        APIQuery = "https://www.googleapis.com/books/v1/volumes?q=" +
                searchTextString + "&maxResults=40";

        BookAsynkTask bookAsynkTask = new BookAsynkTask(this,APIQuery);
        bookAsynkTask.execute();


    }

    private class BookAsynkTask extends AsyncTask<String, Void, String> {

        /** Query URL */
        private String mUrl;

        public BookAsynkTask(Context context, String url) {
            //super(context);
            mUrl = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (mUrl == null) {
                return null;
            }

            String resString = QueryUtils.fetchEarthquakeData(mUrl);
            return resString;
        }

        @Override
        protected void onPostExecute(String result){
            System.out.println("Yippi!..........The title is: " + result);
        }
    }

}