package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchHighestRatedResultsDetails class, used to find and display details
 of the selected movie or tv show
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

public class SearchHighestRatedResultsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //get the query type, which will tell us which layout to call (movies or tv shows)
        int queryType = getIntent().getIntExtra("Query", 0);

        //if query was for most popular movies
        if (queryType == 1) {
            setContentView(R.layout.search_highest_rated_results_movie_details);
            MovieResult mr = (MovieResult)getIntent().getSerializableExtra("RESULT");

            ImageView im = (ImageView)findViewById(R.id.thumbnail);
            TextView title = (TextView)findViewById(R.id.title);
            TextView genre = (TextView)findViewById(R.id.genre);
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
                    //set the poster image
                    imposter.setImageBitmap(poster);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        //if query was for most popular tv shows
        } else if (queryType == 2) {
            setContentView(R.layout.search_highest_rated_results_tvshow_details);
            TVResult mr = (TVResult)getIntent().getSerializableExtra("RESULT");

            ImageView im = (ImageView)findViewById(R.id.thumbnail);
            TextView name = (TextView)findViewById(R.id.name);
            TextView originalName = (TextView)findViewById(R.id.originalName);
            TextView originalLanguage = (TextView)findViewById(R.id.originalLanguage);
            TextView popularity = (TextView)findViewById(R.id.popularity);
            TextView firstAirDate = (TextView)findViewById(R.id.firstAirDate);
            ImageView imposter = (ImageView)findViewById(R.id.poster);
            TextView overviewTV = (TextView)findViewById(R.id.overview);
            TextView voteAverage = (TextView)findViewById(R.id.voteAverage);

            //Populate the data into the template view using the data object
            name.setText("Title : "+mr.getName());
            originalLanguage.setText("Original Language : " + mr.getOriginalLanguage());
            originalName.setText("Original Name : " + mr.getOriginalName());
            voteAverage.setText("Vote Average : " + mr.getVoteAverage() );
            popularity.setText("Popularity : "+mr.getPopularity());
            firstAirDate.setText("Release Year : "+mr.getFirstAirDate());
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
                    //set the poster image
                    imposter.setImageBitmap(poster);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }
}
