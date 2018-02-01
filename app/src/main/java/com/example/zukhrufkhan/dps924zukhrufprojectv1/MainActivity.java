package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
MainActivity, contains button listeners to start the other activites
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button for searching movies
        Button searchMovies = (Button) findViewById(R.id.searchMovie);

        searchMovies.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //handles search movies
                        startActivity(new Intent(MainActivity.this, SearchMovie.class ));

                    }
                }
        );


        //button for searching tv shows
        Button searchTVShows = (Button) findViewById(R.id.searchTVShow);

        searchTVShows.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //handles search tv shows
                        startActivity(new Intent(MainActivity.this, SearchTVShow.class ));

                    }
                }
        );


        //button for searching actors
        Button searchActors = (Button) findViewById(R.id.searchActor);

        searchActors.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //handles search actors
                        startActivity(new Intent(MainActivity.this, SearchActor.class ));

                    }
                }
        );


        //button for searching highest rated items
        Button searchHighestRated = (Button) findViewById(R.id.highestRated);

        searchHighestRated.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //handles search highest rated
                        startActivity(new Intent(MainActivity.this, SearchHighestRated.class ));

                    }
                }
        );


    }
}

