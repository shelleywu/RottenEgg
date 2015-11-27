package com.formovies.shelleywu.rottenegg;

/**
 * Created by shelleywu on 11/27/15.
 */
public final class Config {

    private Config() {
    }

    public static final String YOUTUBE_API_KEY = "####";
    public static final String MOVIE_API_KEY = "?api_key=####";
    public static final String SORT_BY_POPULARITY = "&sort_by=popularity.desc";
    public static final String SORT_BY_RATING = "&sort_by=vote_average.desc";
    public static final String MOVIES = "https://api.themoviedb.org/3/discover/movie";
    public static String REVIEWS = "http://api.themoviedb.org/3/movie/";

}