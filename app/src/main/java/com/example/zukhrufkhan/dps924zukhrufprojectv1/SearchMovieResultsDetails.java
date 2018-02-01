package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchMovieResultsDetails class, used to display details of the selected movie
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SearchMovieResultsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_search_movie_results_details);

        //get the MovieResult from the intent
        MovieResult mr = (MovieResult)getIntent().getSerializableExtra("RESULT");

        ImageView im = (ImageView)findViewById(R.id.thumbnail);
        TextView title = (TextView)findViewById(R.id.title);
        TextView popularity = (TextView)findViewById(R.id.popularity);
        TextView release_year = (TextView)findViewById(R.id.release_year);
        ImageView imposter = (ImageView)findViewById(R.id.poster);
        TextView overviewTV = (TextView)findViewById(R.id.overview);

        //Populate the data into the template view using the data object
        title.setText("Title : "+mr.getTitle());
        popularity.setText("Popularity : "+mr.getPopularity());
        release_year.setText("Release Year : "+mr.getReleaseDate());
        overviewTV.setText(mr.getOverview());

        Bitmap bmp = null;
        Bitmap poster = null;
        try {
            if(mr.getBackdropPath() != null || !mr.getBackdropPath().equals("")){
                bmp = BitmapFactory.decodeStream((InputStream)new URL(mr.getBackdropPath()).getContent());
                //set the thumbnail image
                im.setImageBitmap(bmp);
            }
            if(mr.getPosterPath() != null || !mr.getPosterPath().equals("")){
                poster = BitmapFactory.decodeStream((InputStream)new URL(mr.getPosterPath()).getContent());
                //set the actual poster image
                imposter.setImageBitmap(poster);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


