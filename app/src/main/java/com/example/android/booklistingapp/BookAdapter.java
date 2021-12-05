package com.example.android.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;


public class BookAdapter extends ArrayAdapter<Book> {

    /** Tag for the log messages */
    private static final String LOG_TAG = BookAdapter.class.getSimpleName();


    public BookAdapter(Activity context, ArrayList<Book> bookArrayList) {
        super(context, 0, bookArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image_view);

        if(currentBook.getImageLinkSmallThumbnail().equalsIgnoreCase("---")){
            imageView.setImageBitmap(currentBook.getBitmap());
        }
        else{
            imageView.setImageBitmap(currentBook.getBitmap());
        }



        /*
        if(currentBook.getImageLinkSmallThumbnail().equalsIgnoreCase("---")){
            imageView.setImageResource(R.drawable.baseline_library_books_black_48);
        }
        else{
            //imageView.setImageDrawable(currentBook.getDrawable());
            //loadMapPreview(imageView,currentBook.getImageLinkSmallThumbnail());

            URL myUrl = null;
            try {
                myUrl = new URL("http://www.vaultads.com/wp-content/uploads/2011/03/google-adsense.jpg");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = (InputStream)myUrl.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);

        }
        */



        TextView bookTitleTextView = (TextView) listItemView.findViewById(R.id.booktitle_text_view);
        bookTitleTextView.setText(currentBook.getBookTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);


        String authors = "";

        if(currentBook.getBookAuthorsList() == null ||
                currentBook.getBookAuthorsList().get(0).equalsIgnoreCase("")){
            authors = "---";
        }
        else{
            if(currentBook.getBookAuthorsList().size()>1){
                for(int i = 0 ; i < currentBook.getBookAuthorsList().size() ; i++){
                    authors = authors + currentBook.getBookAuthorsList().get(i).toString() + ", ";
                }
            }
            else{
                authors = currentBook.getBookAuthorsList().get(0);
            }
        }

        authorTextView.setText(authors);

        TextView pubDateTextView = (TextView) listItemView.findViewById(R.id.pubdate_text_view);
        pubDateTextView.setText(currentBook.getBookPublishDate());

        TextView languageTextView = (TextView) listItemView.findViewById(R.id.language_text_view);
        languageTextView.setText(currentBook.getBookLanguage());

        return listItemView;
    }

    public void loadMapPreview (ImageView imageView, String urlString) {
        //start a background thread for networking
        new Thread(new Runnable() {
            public void run(){
                try {
                    //download the drawable
                    final Drawable drawable = Drawable.createFromStream((InputStream) new URL(urlString).getContent(), "src");
                    //edit the view in the UI thread
                    imageView.post(new Runnable() {
                        public void run() {
                            imageView.setImageDrawable(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getLinkImages(String imageLinkStr) throws IOException {

        // Create URL object
        URL url = createUrl(imageLinkStr);

        makeHttpRequest(url);



    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    public static void makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return ;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                //jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving image.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static void readFromStream(InputStream inputStream) throws IOException {
        //StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                //output.append(line);
                line = reader.readLine();
            }
        }
    }

}


