package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchHighestRatedResults class, used to carry out a search query
for most popular movies and tv shows with the API
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

import static com.example.zukhrufkhan.dps924zukhrufprojectv1.SearchMovieResults.*;

public class SearchHighestRatedResults extends Activity {

    public int queryType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_search_highest_rated_results);

        Bundle bundle = getIntent().getExtras();
        //get the query string from the intent
        String query = bundle.getString("query");
        Log.d("FOUND QUERY", " " + query);
        //Query type determines if we should search movies(1) or tv shows(2)
        if (query.equals("1")) {
            queryType = 1;
        } else if (query.equals("2")) {
            queryType = 2;
        }

        //Check if the NetworkConnection is active and connected.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //send the query to the query manager
            new SearchHighestRatedResults.TMDBQueryManager().execute(query);
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
    public void  updateViewWithResultsMovie(final ArrayList<MovieResult> result) {

        Log.d("updateViewWithResults", result.toString());
        ListView listView = new ListView(this);

        SearchMovieResults.MovieAdapter adapter = new SearchMovieResults.MovieAdapter(this, result);
        listView.setAdapter(adapter);

        //Update Activity to show listView
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //intent to display the selected movie with the SearchMovieResultsDetails activity layout
                Intent i = new Intent(getApplication(), SearchMovieResultsDetails.class);

                Bundle mBundle = new Bundle();
                //get the MovieResult of the selected Movie item
                mBundle.putSerializable("RESULT", result.get((int) id));
                i.putExtras(mBundle);
                startActivity(i);
            }
        });
    }

    /*
   This function updates the corresponding layout view with the tv show result set,
   and sets the onItemClickListener for the result selected by the user
    */
    public void updateViewWithResultsTVShow(final ArrayList<TVResult> result) {

        Log.d("updateViewWithResults", result.toString());
        ListView listView = new ListView(this);

        SearchTVShowResults.TVAdapter adapter = new SearchTVShowResults.TVAdapter(this, result);
        listView.setAdapter(adapter);

        //Update Activity to show listView
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //intent to display the selected tv show with the SearchTVShowResultsDetails activity layout
                Intent i = new Intent(getApplication(), SearchTVShowResultsDetails.class);

                Bundle mBundle = new Bundle();
                //attach the TVResult object of the selected Movie item
                mBundle.putSerializable("RESULT", result.get((int) id));
                mBundle.putInt("Query", queryType);
                i.putExtras(mBundle);
                startActivity(i);
            }
        });
    }



    private class TMDBQueryManager extends AsyncTask {

        private final String TMDB_API_KEY = "d5cf82939f2abf1d7e1e48b8d940a0dc";

        protected ArrayList<MovieResult> doInBackgroundMovie(Object... params) {
            try {
                return searchTMDBMovies((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }


        protected ArrayList<TVResult> doInBackgroundTV(Object... params) {
            try {
                return searchTMDBTVShows((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        /*
        This doInBackground function calls the specific doInBackground function
        according to the query type
         */
        @Override
        protected Object doInBackground(Object[] objects) {
            if (queryType == 1) {
                return doInBackgroundMovie(objects);
            } else {
                return doInBackgroundTV(objects);
            }

        }

        /*
        This onPostExecute function calls the specific doInBackground function
        according to the query type
         */
        @Override
        protected void onPostExecute(Object result) {
            if (queryType == 1) {
                updateViewWithResultsMovie((ArrayList<MovieResult>) result);
            } else if (queryType == 2) {
                updateViewWithResultsTVShow((ArrayList<TVResult>) result);
            }

        }


        //search function for movies
        public ArrayList<MovieResult> searchTMDBMovies(String query) throws IOException {
            //query String for API
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/discover/movie");
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
            stringBuilder.append("&sort_by=popularity.desc&include_adult=false" );

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
                return parseResultMovie(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }


        //search function for tv shows
        public ArrayList<TVResult> searchTMDBTVShows(String query) throws IOException {
            //query String for API
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/discover/tv");
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
            stringBuilder.append("&sort_by=popularity.desc");

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
                return parseResultTV(stringify(stream));
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
        private ArrayList<MovieResult> parseResultMovie(String result) {

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
        This function parses the String containing the results and
        parses it into an array of TVResults
         */
        private ArrayList<TVResult> parseResultTV(String result) {
            String streamAsString = result;
            ArrayList<TVResult> results = new ArrayList<TVResult>();

            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                //put the String stream into a JSON array
                JSONArray array = (JSONArray) jsonObject.get("results");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonTVObject = array.getJSONObject(i);

                    TVResult.Builder tvBuilder = TVResult.newBuilder(
                            Integer.parseInt(jsonTVObject.getString("id")),
                            jsonTVObject.getString("name"))
                            .setPosterPath("http://image.tmdb.org/t/p/w500" + jsonTVObject.getString("poster_path"))
                            .setBackdropPath("http://image.tmdb.org/t/p/w500" + jsonTVObject.getString("backdrop_path"))
                            .setOriginalLanguage(jsonTVObject.getString("original_language"))
                            .setOriginalName(jsonTVObject.getString("original_name"))
                            .setPopularity(jsonTVObject.getString("popularity"))
                            .setFirstAirDate(jsonTVObject.getString("first_air_date"))
                            .setOverview(jsonTVObject.getString("overview"));
                    //add the TVResult to the array
                    results.add(tvBuilder.build());

                }
            } catch (JSONException e) {
                System.err.println("TV There has been an error: " + e);
            }

            if (results.isEmpty()) {
                Log.d("TV RESULTS NOT OK", "there are not some results");
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


}
