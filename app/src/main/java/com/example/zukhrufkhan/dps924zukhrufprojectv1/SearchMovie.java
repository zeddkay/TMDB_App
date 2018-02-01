package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchMovie Activity, used to start results
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        Button searchMovies = (Button) findViewById(R.id.buttonSearchMovie);

        searchMovies.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText movieQuery = (EditText) findViewById(R.id.movieQuery);
                         String query = String.valueOf(movieQuery.getText().toString());
                        Intent intent = new Intent(SearchMovie.this, SearchMovieResults.class);
                        intent.putExtra("query", query);
                        //send search movie query to SearchMovieResults for processing
                        startActivity(intent);
                    }
                }
        );
    }


}
