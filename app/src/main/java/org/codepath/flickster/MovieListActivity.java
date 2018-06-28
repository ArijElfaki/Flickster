package org.codepath.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codepath.flickster.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {
    //constants

    //base url for api
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //parameter bane for API key
    public final static String API_KEY_PARAM= "api_key";
    //tag for error logging
    public final static String TAG=  "MovieListActivity";



    //instance feilds
    AsyncHttpClient client;
    // the base url for loading images
    String imageBaseUrl;
    // poster size to use when fethcing images, part of the url
    String posterSize;
    //list of currently playing movies
    ArrayList<Movie> movies;
    // the recycle view
    RecyclerView rvMovies;
    // the adapter wirwd to the recycler view
    MovieAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initalize the client
        client= new AsyncHttpClient();

        //initalize the movies playing
        movies= new ArrayList<>();

        //initalize the adapter
        adapter= new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and te adapter
        rvMovies= (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);


        //get confriguration on app creation
        getConfriguration();

    }

    //get the list of currently playing movies
    private void getNowPlaying(){
        //create the url
        String url= API_BASE_URL+"/movie/now_playing";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// API key always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results= response.getJSONArray("results");
                    for (int i=0; i<results.length();i++){
                        Movie movie= new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify adapter a row was added
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies",e,true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }




    // get the configuration from the API
    public void getConfriguration(){
        //create the url
        String url = API_BASE_URL+"/configuration";
        //set the request parameters
        RequestParams params= new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images= response.getJSONObject("images");
                    //get the image base url
                    imageBaseUrl=images.getString("secure_base_url");
                    //get poster size
                    JSONArray posterSizeOptions=images.getJSONArray("poster_sizes");
                    //use option at 3 or default to w342
                    posterSize=posterSizeOptions.optString(3, "w342");
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", imageBaseUrl,posterSize));
                    //get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration",e,true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handles errors, log and alert user
    public void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser){
            //show a toast w/error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}
