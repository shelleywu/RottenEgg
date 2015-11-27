package com.formovies.shelleywu.rottenegg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DetailMovie extends AppCompatActivity {
    private static final String EXTRA_KEY = "detailMovie";
    private Movie mMovie;

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailMovie.class);
        intent.putExtra(EXTRA_KEY, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Movie movie = getIntent().getParcelableExtra(EXTRA_KEY);
        mMovie = movie;
        setContentView(R.layout.activity_detail_movie);

        Fragment fragment = DetailMovieFragment.newInstance(mMovie);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.activity_detail_movie_fragmentContainer, fragment)
                .commit();

    }
}