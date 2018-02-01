package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchActorResultsDetails class, used to find and display details of the selected actor
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchActorResultsDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_search_actor_results_details);

        //Get the actors ID string from the Intent
        String id = getIntent().getStringExtra("ID");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //New query to find details by ID, because SearchActorResults TMDB query
            // does not return all details about resulting actors,
            // so we will search for the details using the ID we have received
            new TMDBQueryManager().execute(id);
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
        }

    }

    /*
    This function updates the corresponding layout view with the actor's details
     */
    public void updateViewWithResults(final ActorResult result) {
        ImageView im = (ImageView) findViewById(R.id.profile);
        TextView name = (TextView) findViewById(R.id.name);
        TextView alsoKnownAs = (TextView) findViewById(R.id.alsoKnownAs);
        TextView popularity = (TextView) findViewById(R.id.popularity);
        TextView birthday = (TextView) findViewById(R.id.birthday);
        TextView placeOfBirth = (TextView) findViewById(R.id.placeOfBirth);
        TextView biography = (TextView) findViewById(R.id.biography);
        // Populate the data into the template view using the data object
        name.setText("Name : " + result.getName());
        alsoKnownAs.setText("Also Known As :" + result.getAlsoKnownAs());
        popularity.setText("Popularity : " + result.getPopularity());
        birthday.setText("Birthday : " +result.getBirthday());
        placeOfBirth.setText("Place of Birth : " + result.getPlaceOfBirth());
        biography.setText("Biography : " + result.getBiography());

        Bitmap bmp = null;
        try {
            if(result.getProfilePath() != null || !result.getProfilePath().equals("")){
                bmp = BitmapFactory.decodeStream((InputStream)new URL(result.getProfilePath()).getContent());
                //set the thumbnail image
                im.setImageBitmap(bmp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    A class that manages the find by ID query
     */
    public class TMDBQueryManager extends AsyncTask {

        private final String TMDB_API_KEY = "d5cf82939f2abf1d7e1e48b8d940a0dc";

        @Override
        protected ActorResult doInBackground(Object... params) {
            try {
                return searchTMDB((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            updateViewWithResults((ActorResult) result);
        }


        //search function for actor details
        public ActorResult searchTMDB(String query) throws IOException {
            //query String for API to find person details
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/person/");
            stringBuilder.append(query);
            stringBuilder.append("?api_key=" + TMDB_API_KEY);

            URL url = new URL(stringBuilder.toString());

            InputStream stream = null;
            try {
                //Establish a connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.connect();

                stream = conn.getInputStream();
                //call parseResult with the resulting input stream
                return parseResult(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        /*
       This function parses the String containing the result and
       parses it into an ActorResult
        */
        private ActorResult parseResult(String result) {
            String streamAsString = result;
            ActorResult results = new ActorResult();

            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                //no array needed as we are only returning one ActorResult

                JSONObject jsonActorObject = jsonObject;

                    ActorResult.Builder actorBuilder = ActorResult.newBuilder(
                            Integer.parseInt(jsonActorObject.getString("id")),
                            jsonActorObject.getString("name"))
                            .setName(jsonActorObject.getString("name"))
                            .setPlaceOfBirth(jsonActorObject.optString("place_of_birth", "NULL"))
                            .setPopularity(jsonActorObject.getString("popularity"))
                            .setProfilePath("http://image.tmdb.org/t/p/w500" + jsonActorObject.getString("profile_path"))
                            .setBiography(jsonActorObject.optString("biography", "NULL"))
                            .setBirthday(jsonActorObject.optString("birthday", "NULL"))
                            .setAlsoKnownAs(jsonActorObject.optString("also_known_as", "NULL"));
                    results = (actorBuilder.build());

                } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return results;
        }

        /*
        This function takes in the InputStream and returns a BufferedReader
         */
        public String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }

    // No ActorAdapter class needed as we are not creating any new Listviews or Arrays of actors


}



