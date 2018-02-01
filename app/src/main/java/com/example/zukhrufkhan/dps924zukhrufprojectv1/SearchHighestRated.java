package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchHighestRated Activity, used to start results
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SearchHighestRated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_highest_rated);

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup4);
        group.clearCheck(); //clear any selected RadioButtons
        Button searchHighestRated = (Button) findViewById(R.id.buttonsearchHighestRated);

        searchHighestRated.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        RadioButton movies = (RadioButton) findViewById(R.id.radioButtonMovies);
                        RadioButton tvShows = (RadioButton) findViewById(R.id.radioButtonTVShows);
                        String query = "0";
                        //use query string to store movies or tv shows, depending on which radiobutton is selected
                        if (movies.isChecked()) {
                            query = "1";
                        } else if (tvShows.isChecked()) {
                            query = "2";
                        }
                        Intent intent = new Intent(SearchHighestRated.this, SearchHighestRatedResults.class);
                        intent.putExtra("query", query);
                        //send the type of media item to show highest rated for to SearchTVShowResults for processing
                        startActivity(intent);
                    }
                }
        );
    }
}
