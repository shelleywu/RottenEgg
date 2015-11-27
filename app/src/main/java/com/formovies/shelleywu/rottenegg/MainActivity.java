package com.formovies.shelleywu.rottenegg;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity implements MainActivityFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new MainActivityFragment();
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = DetailMovie.newIntent(this, movie);
            startActivity(intent);
        } else {
            Fragment fragment = DetailMovieFragment.newInstance(movie);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        }

    }
}