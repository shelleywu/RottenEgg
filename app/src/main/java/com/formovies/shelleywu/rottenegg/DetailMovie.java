package com.formovies.shelleywu.rottenegg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Fragment fragment = DetailMovieFragment.newInstance(mMovie);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.activity_detail_movie_fragmentContainer, fragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id==android.R.id.home) {
        finish();
        }
        return super.onOptionsItemSelected(item);
    }
}