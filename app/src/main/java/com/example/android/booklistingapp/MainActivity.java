package com.example.android.booklistingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

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

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(1, null, this);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, APIQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> booksArrayList) {

        System.out.println("arraysize= " + booksArrayList.size());

        int index = 0;

        System.out.println("---------------------------");
        System.out.println("BookId: " + booksArrayList.get(index).getBookID());
        System.out.println("BookTitle: " + booksArrayList.get(index).getBookTitle());
        for(int i = 0 ; i < booksArrayList.get(index).getBookAuthorsList().size() ; i++){
            System.out.println("Author " + i + ": " + booksArrayList.get(index).getBookAuthorsList().get(i));
        }

        System.out.println("Published date: " + booksArrayList.get(index).getBookPublishDate());
        System.out.println("Book Language: " + booksArrayList.get(index).getBookLanguage());
        System.out.println("ImageLink: " + booksArrayList.get(index).getImageLinkSmallThumbnail());
        System.out.println("---------------------------");

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

    }

    public static class BookLoader extends AsyncTaskLoader<List<Book>> {


        /** Query URL */
        private String mUrl;


        public BookLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Book> loadInBackground() {

            if (mUrl == null) {
                return null;
            }

            List<Book> booksArrayList = QueryUtils.fetchEarthquakeData(mUrl);
            return booksArrayList;
        }
    }

}