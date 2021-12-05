package com.example.android.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;


public class BookAdapter extends ArrayAdapter<Book> {



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
            imageView.setImageResource(R.drawable.baseline_library_books_black_48);
        }
        else{
            //imageView.setImageDrawable(currentBook.getDrawable());
            loadMapPreview(imageView,currentBook.getImageLinkSmallThumbnail());
        }


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
}


