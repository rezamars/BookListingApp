package com.example.android.booklistingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    //private static String APIQuery = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    private String searchTextString = "YYYYY";
    public static String APIQuery;
    private LoaderManager loaderManager;

    /** Adapter for the list of books */
    private BookAdapter mAdapter;


    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void startSearch(View textView){

        progressBar.setVisibility(View.VISIBLE);

        TextView searchTextView = findViewById(R.id.searchText);
        searchTextString = searchTextView.getText().toString();

        APIQuery = "https://www.googleapis.com/books/v1/volumes?q=" +
                searchTextString + "&maxResults=40";

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(loaderManager != null){
            loaderManager.getLoader(1).reset();
            loaderManager.destroyLoader(1);
        }


        if(isConnected){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);

        }
        else{
            System.out.println("ERROR ! No network connection." );
            progressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internet connection");
        }

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);


    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, APIQuery);
    }


    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> booksArrayList) {

        if(booksArrayList.isEmpty()||booksArrayList == null){
            System.out.println("booksArrayList is empty!" );
        }
        else{
            System.out.println("arraysize= " + booksArrayList.size());

            for(int p = 0 ; p < booksArrayList.size() ; p++){
                //System.out.println(p + ": " + "aaaaaaaaaaaaaaaaaaa....BookTitle: " + booksArrayList.get(p).getBookTitle());
            }

            /*
            int index = 3;

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
            */
        }

        progressBar.setVisibility(View.INVISIBLE);


        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (booksArrayList != null && !booksArrayList.isEmpty()) {
            mAdapter.addAll(booksArrayList);
        }
        else{
            // Set empty state text to display "No books found."
            mEmptyStateTextView.setText("No books found!");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public static class BookLoader extends AsyncTaskLoader<List<Book>> {


        public BookLoader(Context context, String url) {
            super(context);
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

            if (APIQuery == null) {
                return null;
            }

            List<Book> booksArrayList = QueryUtils.fetchEarthquakeData(APIQuery);

            /*
            Drawable drawable = null;
            for(int w = 0 ; w < booksArrayList.size() ; w++){
                try {
                     drawable = Drawable.createFromStream((InputStream) new
                            URL(booksArrayList.get(w).getImageLinkSmallThumbnail()).getContent(), "src");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(booksArrayList.get(w).getImageLinkSmallThumbnail().equalsIgnoreCase("---")){
                    //booksArrayList.get(w).setImageDrawable(R.drawable.baseline_library_books_black_48);
                }
                else{
                    booksArrayList.get(w).setImageDrawable(drawable);
                }

            }
            */

            return booksArrayList;
        }
    }

}