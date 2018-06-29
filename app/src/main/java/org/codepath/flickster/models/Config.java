package org.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    // the base url for loading images
    String imageBaseUrl;
    // poster size to use when fethcing images, part of the url
    String posterSize;
    // backdrop size to use when fetching images
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images= object.getJSONObject("images");
        //get the image base url
        imageBaseUrl=images.getString("secure_base_url");
        //get poster size
        JSONArray posterSizeOptions=images.getJSONArray("poster_sizes");
        //use option at 3 or default to w342
        posterSize=posterSizeOptions.optString(3, "w342");
        // parse the backdrop sizes and use the option at index
        JSONArray backdropSizeOptions=images.getJSONArray("backdrop_sizes");
        backdropSize= backdropSizeOptions.optString(1,"w780");

    }


    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl,size,path);
    }


    public String getImageBaseUrl() {
        return imageBaseUrl;
    }


    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
