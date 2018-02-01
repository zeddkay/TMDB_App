package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchTVShow Activity, used to start results
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchTVShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tvshow);

        Button searchTVShows = (Button) findViewById(R.id.buttonSearchTV);

        searchTVShows.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText tvQuery = (EditText) findViewById(R.id.tvQuery);
                        String query = String.valueOf(tvQuery.getText().toString());
                        Intent intent = new Intent(SearchTVShow.this, SearchTVShowResults.class);
                        intent.putExtra("query", query);
                        //send search tvshow query to SearchTVShowResults for processing
                        startActivity(intent);
                    }
                }
        );

    }
}
