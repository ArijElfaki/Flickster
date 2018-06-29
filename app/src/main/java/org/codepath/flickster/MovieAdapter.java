package org.codepath.flickster;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.codepath.flickster.models.Config;
import org.codepath.flickster.models.Movie;
import org.parceler.Parcels;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String imageUrl1="url1";
    public static final String imageUrl2="url2";

    //List of movies
    ArrayList<Movie> movies;
    //config needed for image urls
    Config config;
    //context for rendering
    Context context;
    public static final String pos="0";

    //initalize the list
    public MovieAdapter(ArrayList<Movie> movies){
        this.movies=movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates a new view

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        // get the content and create the inflater
        context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        //create the view using the item_movie layout
        View movieView= inflater.inflate(R.layout.item_movie,parent,false);
        // return a new view holder
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        //get the movie data at the specified position
        Movie movie= movies.get(position);
        //populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait= context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        //build url for poster image
        String imageUrl = null;

        // in portrait mode, load the poster image
        if (isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else {
            // load the backdrop image
            imageUrl=config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get the correct placeholder and imageview for the current orientation
        int placeholderId= isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView= isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;


        // load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCorners(25))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        //track the view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView){
            super (itemView);
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage= (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview= (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle= (TextView)itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                intent.putExtra(imageUrl1, config.getImageUrl(config.getPosterSize(), movie.getPosterPath()));
                intent.putExtra(imageUrl2, config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath()));
                // show the activity
                context.startActivity(intent);
            }
        }
    }




}
