package org.codepath.flickster;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.codepath.flickster.models.Movie;
import org.parceler.Parcels;

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
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView frontIcon;
    ImageView back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvOverview.setMovementMethod(new ScrollingMovementMethod());
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        frontIcon= (ImageView) findViewById(R.id.frontIcon);
        back= (ImageView) findViewById(R.id.backDrop);



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
    }


}

