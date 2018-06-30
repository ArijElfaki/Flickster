package org.codepath.flickster;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codepath.flickster.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static org.codepath.flickster.MovieAdapter.imageUrl1;
import static org.codepath.flickster.MovieAdapter.imageUrl2;


public class MovieDetailsActivity extends AppCompatActivity {

    //base url for api
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //parameter bane for API key
    public final static String API_KEY_PARAM= "api_key";
    //tag for error logging
    public final static String TAG=  "MovieListActivity";


    // the movie to display
    Movie movie;

    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage)RatingBar rbVoteAverage;
    @BindView(R.id.frontIcon)ImageView frontIcon;
    @BindView(R.id.backDrop)ImageView back;

    String id;

    AsyncHttpClient client;
    public  final static  String youtubeId= "url";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // resolve the view objects
        ButterKnife.bind(this);
       // tvTitle = (TextView) findViewById(R.id.tvTitle);
       // tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvOverview.setMovementMethod(new ScrollingMovementMethod());
        //rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

       // frontIcon= (ImageView) findViewById(R.id.frontIcon);
       // back= (ImageView) findViewById(R.id.backDrop);



        //position= getIntent().getStringExtra(pos);


        //Context context =
        GlideApp.with(this)
                .load(getIntent().getStringExtra(imageUrl2))
                // .transform(new RoundedCorners(25))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(back);

        GlideApp.with(this)
                .load(getIntent().getStringExtra(imageUrl1))
                .transform(new RoundedCorners(25))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(frontIcon);


        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


        //initalize the client
        client= new AsyncHttpClient();
        getMovie(movie);


    }

    public void loadVideo(View view){
        if (movie.getId()!=null){
            Intent i = new Intent(this, MovieTrailerActivity.class);
            i.putExtra(youtubeId, id);
            Log.i(TAG, String.format("Passing youtube video id "+youtubeId));
            this.startActivity(i);
        }
    }


    // get the movie configuration from the API
    public void getMovie(Movie movie2){
        //create the url
       String url = API_BASE_URL+"/movie/"+movie2.getId()+"/videos";
        //set the request parameters
        RequestParams params= new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results= response.getJSONArray("results");
                    JSONObject first= results.getJSONObject(0);
                    id=first.getString("key");
                    Log.i(TAG, String.format("Loaded youtube video id "+id));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}

