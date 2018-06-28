package org.codepath.flickster;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.codepath.flickster.models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //List of movies
    ArrayList<Movie> movies;

    //initalize the list
    public MovieAdapter(ArrayList<Movie> movies){
        this.movies=movies;
    }

    // creates and inflates a new view

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        // get the content and create the inflater
        Context context= parent.getContext();
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

        //TODO - set image using Glide

    }
//hi
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{

        //track the view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView){
            super (itemView);
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview= (TextView) itemView.findViewById(R.id.tvOverviw);
            tvTitle= (TextView)itemView.findViewById(R.id.tvTitle);

        }
    }


}
