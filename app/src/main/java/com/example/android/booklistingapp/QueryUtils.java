package com.example.android.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        String resJson = "";

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Book> booksArrayList = new ArrayList<Book>();

        Book bookObject;

        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            for(int loopIndex = 0 ; loopIndex < itemsArray.length() ; loopIndex++){

                bookObject = new Book();

                JSONObject firstItem = itemsArray.getJSONObject(loopIndex);

                String idString = firstItem.getString("id");

                JSONObject volumeInfo = firstItem.getJSONObject("volumeInfo");

                String titleString = volumeInfo.getString("title");

                JSONArray authorsJsonArray;
                ArrayList<String> authorsList = new ArrayList<String>();
                String emptyString = "---";

                if(volumeInfo.has("authors")){
                    authorsJsonArray = volumeInfo.getJSONArray("authors");
                    for(int i = 0 ; i < authorsJsonArray.length() ; i++){
                        authorsList.add(authorsJsonArray.getString(i));
                    }
                }
                else{
                    authorsList.add(emptyString);
                }

                String publishDateString = "";
                if(volumeInfo.has("publishedDate")){
                    publishDateString = volumeInfo.getString("publishedDate");
                }
                else{
                    publishDateString = "---";
                }

                String languageString = volumeInfo.getString("language");

                String infoLink = "";
                if(volumeInfo.has("infoLink")){
                    infoLink = volumeInfo.getString("infoLink");
                }
                else{
                    infoLink = "---";
                }


                String imageLinkString = "";

                if(volumeInfo.has("imageLinks")){
                    JSONObject imageLinksJson = volumeInfo.getJSONObject("imageLinks");
                    imageLinkString = imageLinksJson.getString("smallThumbnail");
                }
                else{
                    imageLinkString = "---";
                }


                bookObject.setBookID(idString);
                bookObject.setBookTitle(titleString);
                bookObject.setBookAuthorsList(authorsList);
                bookObject.setBookPublishDate(publishDateString);
                bookObject.setBookLanguage(languageString);
                bookObject.setInfoLink(infoLink);
                bookObject.setImageLinkSmallThumbnail(imageLinkString);


                String imageUrl = "";

                if(imageLinkString.equalsIgnoreCase("---")){
                    //imageUrl = "http://www.clker.com/cliparts/n/m/t/4/w/d/libro-gran-md.png";
                    //imageUrl = "http://www.clker.com/cliparts/a/f/5/7/11949859662103344033old_book_lumen_design_st_01.svg.med.png";
                    imageUrl = "https://www.clker.com/cliparts/e/8/0/e/1206580017363707510barretr_Book.svg.med.png";
                }
                else{
                    imageUrl = imageLinkString;
                }

                Bitmap bitmap = null;
                InputStream in = new java.net.URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                bookObject.setBitmap(bitmap);

                booksArrayList.add(bookObject);

            }

        } catch (JSONException | MalformedURLException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return booksArrayList;
    }

    public static List<Book> fetchEarthquakeData(String requestUrl) {

        /*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> booksArrayList = extractFeatureFromJson(jsonResponse);

        return booksArrayList;
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

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
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
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
