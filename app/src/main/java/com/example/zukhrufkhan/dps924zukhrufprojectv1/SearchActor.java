package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchActor Activity, used to start results activity
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class SearchActor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_actor);

        Button searchActors = (Button) findViewById(R.id.buttonSearchActor);

        searchActors.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText actorQuery = (EditText) findViewById(R.id.actorQuery);
                        String query = String.valueOf(actorQuery.getText().toString());
                        Intent intent = new Intent(SearchActor.this, SearchActorResults.class);
                        intent.putExtra("query", query);
                        //send search actor query to SearchActorResults for processing
                        startActivity(intent);
                    }
                }
        );
    }


}
