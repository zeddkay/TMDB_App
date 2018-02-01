package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchMovieResults class, used to carry out a search query for movies with the API
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
import android.widget.EditText;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.webkit.WebChromeClient.FileChooserParams.parseResult;

public class SearchMovieResults extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_search_movie_results);

        Bundle bundle = getIntent().getExtras();
        //get the query string from the intent
        String query = bundle.getString("query");
        Log.d("FOUND QUERY", query);

        //Check if the NetworkConnection is active and connected.
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
    This function updates the corresponding layout view with the movie result set,
    and sets the onItemClickListener for the result selected by the user
     */
    public void  updateViewWithResults(final ArrayList<MovieResult> result) {

        Log.d("updateViewWithResults", result.toString());
        ListView listView = new ListView(this);

        MovieAdapter adapter = new MovieAdapter(this, result);
        listView.setAdapter(adapter);

        //Update Activity to show listView
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplication(), SearchMovieResultsDetails.class);

                Bundle mBundle = new Bundle();
                //put the MovieResult of the selected Movie item in the intent
                mBundle.putSerializable("RESULT", result.get((int) id));
                i.putExtras(mBundle);

                startActivity(i);
            }
        });
    }

    /*
    A class that manages the search queries
     */
    private class TMDBQueryManager extends AsyncTask {

        private final String TMDB_API_KEY = "d5cf82939f2abf1d7e1e48b8d940a0dc";

        @Override
        protected ArrayList<MovieResult> doInBackground(Object... params) {
            try {
                return searchTMDB((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            updateViewWithResults((ArrayList<MovieResult>) result);
        }


        //search function for movies
        public ArrayList<MovieResult> searchTMDB(String query) throws IOException {
            //query String for API
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/search/movie");
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
       parses it into an array of MovieResults
        */
        private ArrayList<MovieResult> parseResult(String result) {
            String streamAsString = result;
            ArrayList<MovieResult> results = new ArrayList<MovieResult>();

            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                //put the String stream into a JSON array
                JSONArray array = (JSONArray) jsonObject.get("results");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonMovieObject = array.getJSONObject(i);

                    MovieResult.Builder movieBuilder = MovieResult.newBuilder(
                            Integer.parseInt(jsonMovieObject.getString("id")),
                            jsonMovieObject.getString("title"))
                            .setBackdropPath("http://image.tmdb.org/t/p/w500" + jsonMovieObject.getString("backdrop_path"))
                            .setOriginalTitle(jsonMovieObject.getString("original_title"))
                            .setPopularity(jsonMovieObject.getString("popularity"))
                            .setPosterPath("http://image.tmdb.org/t/p/w500" + jsonMovieObject.getString("poster_path"))
                            .setReleaseDate(jsonMovieObject.getString("release_date"))
                            .setOverview(jsonMovieObject.getString("overview"));
                    //add the MovieResult to the array
                    results.add(movieBuilder.build());

                }
            } catch (JSONException e) {
                System.err.println(e);
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
   An adapter for the resulting array of MovieResults
    */
    public static class MovieAdapter extends ArrayAdapter<MovieResult> {
        public MovieAdapter(Context context, ArrayList<MovieResult> movies) {
            super(context, 0, movies);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Get the data item for this position
            MovieResult movie = getItem(position);
            //Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_search_movie_results, parent, false);
            }
            //Lookup view for data population
            ImageView im = (ImageView) convertView.findViewById(R.id.thumbnail);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView popularity = (TextView) convertView.findViewById(R.id.popularity);
            TextView release_year = (TextView) convertView.findViewById(R.id.release_year);
            //Populate the data into the template view using the data object
            title.setText("Title : " + movie.getTitle());

            popularity.setText("Popularity : " + movie.getPopularity());
            release_year.setText("Release Year : " + movie.getReleaseDate());

            Bitmap bmp = null;
            try {
                if (movie.getBackdropPath() != null || !movie.getBackdropPath().equals("")) {
                    bmp = BitmapFactory.decodeStream((InputStream) new URL(movie.getBackdropPath()).getContent());
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