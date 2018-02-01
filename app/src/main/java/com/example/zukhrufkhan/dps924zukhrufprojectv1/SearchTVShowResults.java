package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/*
SearchTVShowResults class, used to carry out a search query for tv shows with the API
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

public class SearchTVShowResults extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_search_tvshow_results);

        Bundle bundle = getIntent().getExtras();
        //get the query string from the intent
        String query = bundle.getString("query");
        Log.d("FOUND QUERY", query);

        //Check if the NetworkConnection is active and connected.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //send the query to the query manager
            new SearchTVShowResults.TMDBQueryManager().execute(query);
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
        }


    }


    /*
    This function updates the corresponding layout view with the tv show result set,
    and sets the onItemClickListener for the result selected by the user
    */
    public void updateViewWithResults(final ArrayList<TVResult> result) {

        Log.d("updateViewWithResults", result.toString());
        ListView listView = new ListView(this);

        SearchTVShowResults.TVAdapter adapter = new SearchTVShowResults.TVAdapter(this, result);
        listView.setAdapter(adapter);

        //Update Activity to show listView
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplication(), SearchTVShowResultsDetails.class);

                Bundle mBundle = new Bundle();
                //put the TVResult of the selected TVShow item in the intent
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
        protected ArrayList<TVResult> doInBackground(Object... params) {
            try {
                return searchTMDB((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            updateViewWithResults((ArrayList<TVResult>) result);
        }


        //search function for tv shows
        public ArrayList<TVResult> searchTMDB(String query) throws IOException {
            //query String for API
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/search/tv");
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
        parses it into an array of TVResults
         */
        private ArrayList<TVResult> parseResult(String result) {
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

    /*
    An adapter for the resulting array of TVResults
     */
    public static class TVAdapter extends ArrayAdapter<TVResult> {

        public TVAdapter(Context context, ArrayList<TVResult> tvs) {
            super(context, 0, tvs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Get the data item for this position
            TVResult tv = getItem(position);
            //Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_search_tvshow_results, parent, false);
            }
            //Lookup view for data population
            ImageView im = (ImageView) convertView.findViewById(R.id.thumbnail);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView popularity = (TextView) convertView.findViewById(R.id.popularity);
            TextView firstAirDate = (TextView) convertView.findViewById(R.id.firstAirDate);

            name.setText("Title : " + tv.getName());

            popularity.setText("Popularity : " + tv.getPopularity());
            firstAirDate.setText("First air date : " + tv.getFirstAirDate());

            Bitmap bmp = null;
            try {
                if (tv.getBackdropPath() != null || !tv.getBackdropPath().equals("")) {
                    bmp = BitmapFactory.decodeStream((InputStream) new URL(tv.getBackdropPath()).getContent());
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
