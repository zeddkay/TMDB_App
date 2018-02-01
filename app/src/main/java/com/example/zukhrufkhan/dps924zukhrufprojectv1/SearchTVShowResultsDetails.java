package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchTVShowResultsDetails class, used to display details of the selected tv show
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SearchTVShowResultsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_search_tvshow_results_details);

        //get the TVResult from the Intent
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
                //set the actual poster image
                imposter.setImageBitmap(poster);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
