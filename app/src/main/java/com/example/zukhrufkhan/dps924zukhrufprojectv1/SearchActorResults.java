package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchActorResults class, used to carry out a search query for actors with the API
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchActorResults extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_search_actor_results);

        //get the query string from the intent
        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("query");
        Log.d("FOUND QUERY", query);

        //check if the NetworkConnection is active and connected
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //send the query to the query manager
            new TMDBQueryManager().execute(query);
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
        }


    }

    /*
   This function updates the corresponding layout view with the result set,
   and sets the onItemClickListener for the result selected by the user
    */
    public void updateViewWithResults(final ArrayList<ActorResult> result) {

        Log.d("updateViewWithResults", result.toString());
        ListView listView = new ListView(this);

        ActorAdapter adapter = new ActorAdapter(this, result);
        listView.setAdapter(adapter);

        //Update activity to show the listView
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplication(), SearchActorResultsDetails.class);

                /*
                get the ID of the selected Actor item, so it can be sent to the ActorResultsDetails activity,
                which will use the id in a find actor by ID query to get more details about them
                */
                int idInt = result.get((int)id).getId();
                String actorID = Integer.toString(idInt);
                i.putExtra("ID", actorID);
                startActivity(i);

            }
        });
    }

    /*
     A class that manages the search queries
     */
    public class TMDBQueryManager extends AsyncTask {

        private final String TMDB_API_KEY = "d5cf82939f2abf1d7e1e48b8d940a0dc";

        @Override
        protected ArrayList<ActorResult> doInBackground(Object... params) {
            try {
                return searchTMDB((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {

            updateViewWithResults((ArrayList<ActorResult>) result);

        }

        //search function for actors
        public ArrayList<ActorResult> searchTMDB(String query) throws IOException {

            //query String for API
            StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("http://api.themoviedb.org/3/search/person");
                    stringBuilder.append("?api_key=" + TMDB_API_KEY);
                    stringBuilder.append("&query=" + query);


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
        This function parses the String containing the results and
        parses it into an array of ActorResults
         */
        private ArrayList<ActorResult> parseResult(String result) {
            String streamAsString = result;
            ArrayList<ActorResult> results = new ArrayList<ActorResult>();

            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                //put the String stream into a JSON array
                JSONArray array = (JSONArray) jsonObject.get("results");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonActorObject = array.getJSONObject(i);

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
                    //add the ActorResult to the array
                    results.add(actorBuilder.build());


                }
            } catch (JSONException e) {
                System.err.println("There has been an error: " + e);
            }

            if (results.isEmpty()) {
                Log.d("RESULTS NOT OK", "there are not some results");
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


    /*
    An adapter for the resulting array of ActorResults
     */
    public static class ActorAdapter extends ArrayAdapter<ActorResult> {
        public ActorAdapter(Context context, ArrayList<ActorResult> actors) {
            super(context, 0, actors);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Get the data item for this position
            ActorResult actor = getItem(position);
            //Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_search_actor_results, parent, false);
            }
            //Lookup view for data population
            ImageView im = (ImageView) convertView.findViewById(R.id.profile);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView popularity = (TextView) convertView.findViewById(R.id.popularity);
            //Populate the data into the template view using the data object
            name.setText("Name : " + actor.getName());
            popularity.setText("Popularity : " + actor.getPopularity());

            Bitmap bmp = null;
            try {
                if (actor.getProfilePath() != null || !actor.getProfilePath().equals("")) {
                    bmp = BitmapFactory.decodeStream((InputStream) new URL(actor.getProfilePath()).getContent());
                    //set the thumbnail image
                    im.setImageBitmap(bmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Return the completed view to render on screen
            return convertView;
        }
    }




}
