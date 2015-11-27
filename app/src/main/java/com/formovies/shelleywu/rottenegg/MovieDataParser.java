package com.formovies.shelleywu.rottenegg;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shelleywu on 11/27/15.
 */
public class MovieDataParser {

    String data;
    public static ArrayList<Movie> movies = null;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_ORIGINAL_TITLE = "original_title";
    public static final String TAG_POSTER_PATH = "poster_path";
    private static final String TAG_OVERVIEW = "overview";
    private static final String TAG_USER_RATING = "vote_average";
    private static final String TAG_RELEASE_DATE = "release_date";
    private static final String TAG_ID = "id";


    public MovieDataParser(String data)
    {
        this.data = data;
    }

    public ArrayList<Movie> getAllMovies()
    {
        return movies;
    }


    public ArrayList<Movie> parseMovies()
    {
        ArrayList<Movie> alltheMovies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray originalTitleArray = jsonObject.getJSONArray(TAG_RESULTS);

            for(int i = 0; i < originalTitleArray.length(); i++)
            {
                JSONObject movieresults = originalTitleArray.getJSONObject(i);

                String originalTitle = "";
                String overview = "";
                String posterPath = "";
                String user_rating = "";
                String release_date = "";
                String id = "";

                originalTitle = movieresults.getString(TAG_ORIGINAL_TITLE);

                posterPath = movieresults.getString(TAG_POSTER_PATH);

                id = movieresults.getString(TAG_ID);

                overview = movieresults.getString(TAG_OVERVIEW);

                user_rating = movieresults.getString(TAG_USER_RATING);

                release_date = movieresults.getString(TAG_RELEASE_DATE);

                Movie m = new Movie(originalTitle, overview, posterPath, user_rating, release_date, id);
                alltheMovies.add(m);

            }
        } catch (JSONException e) {
            Log.e("JSONException", "JSON parsing is wrong in MovieDataParser");
        }

        movies = alltheMovies;
        return alltheMovies;
    }
}