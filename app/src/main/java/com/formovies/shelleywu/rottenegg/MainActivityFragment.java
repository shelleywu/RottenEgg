package com.formovies.shelleywu.rottenegg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> mMovies = new ArrayList<>();
    private Callbacks mCallbacks;

    //this works with favorites
    public static final String PREFS = "Favorites";
    public static ArrayList<String> favemovies = new ArrayList<>();
    public static ArrayList<Movie> favorites = new ArrayList<>();

    Movie favemovie;

    public MainActivityFragment() {
    }

    public interface Callbacks {
        void onMovieSelected(Movie movie);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_movie_list_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        new JSONTask().execute(Config.MOVIES + Config.MOVIE_API_KEY + Config.SORT_BY_POPULARITY);
        setHasOptionsMenu(true);
        return rootView;
    }

    public void update() {
        if (mAdapter == null) {
            mAdapter = new MovieAdapter(mMovies);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMovieList(mMovies);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_popular:
                new JSONTask().execute(Config.MOVIES + Config.MOVIE_API_KEY + Config.SORT_BY_POPULARITY);
                return true;

            case R.id.menu_item_rated:
                new JSONTask().execute(Config.MOVIES + Config.MOVIE_API_KEY + Config.SORT_BY_RATING);
                return true;

            case R.id.menu_item_favorite:

                sortFavorites();
                return true;

        }

        return true;
    }

    public void sortFavorites()
    {
            mMovies = favorites;

            //gridViewMovies.setAdapter(null);


        SharedPreferences sp = getActivity().getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.contains("movie")) {
            int position = sp.getInt("movie", 0);
            ArrayList<Movie> m = MovieDataParser.movies;

            for(int i = 0; i < m.size(); i++)
            {
                String pos = m.get(i).getId();
                if(Integer.parseInt(pos) == position)
                {
                    favemovie = m.get(i);
                }
            }

            favorites.add(favemovie);
            favemovies.add("http://image.tmdb.org/t/p/w500/" + favemovie.getPoster());
            editor.remove("movie");
            editor.apply();
        }

        update();
        /**if(favemovies.size() > 0)
        {

            SampleGridViewAdapter sg = new SampleGridViewAdapter(getApplicationContext(), favemovies);

            gridViewMovies.setAdapter(sg);
            gridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent intent = new Intent(MainActivity.this, MovieDescription.class);
                    Bundle movie = new Bundle();
                    movie.putInt("positionfave", position);
                    intent.putExtras(movie);
                    startActivity(intent);


                }
            });
        }**/

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<Movie> movieList;

        public MovieAdapter(List<Movie> movieList) {
            this.movieList = movieList;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_movie, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            holder.bindMovie(movieList.get(position));
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }

        public void setMovieList(List<Movie> movies) {
            movieList = movies;
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView mMovieThumbNail;
        private String mImageBaseUrl = "http://image.tmdb.org/t/p/";
        private String mImageWidth = "w500";
        private Movie mMovie;

        public MovieHolder(View itemView) {
            super(itemView);
            mMovieThumbNail = (ImageView) itemView.findViewById(R.id.list_item_movie_imageView);
            mMovieThumbNail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mMovie != null) {
                        mCallbacks.onMovieSelected(mMovie);
                    }
                }
            });
        }

        public void bindMovie(Movie result) {
            mMovie = result;
            String url = mImageBaseUrl + mImageWidth + "/" + mMovie.getPoster();

            Picasso.with(getActivity())
                    .load(url)
                    .resize(500, 500)
                    .placeholder(R.drawable.rottenegg)
                    .into(mMovieThumbNail);

        }
    }

    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]); //get my api key
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                Log.e("MalformedURL", "Look in your main activity, it probably has the wrong url");
            } catch (IOException e) {
                Log.e("IOException", "Can't open url in main activity.");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e("IOException", "can't close reader in main activity");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MovieDataParser parsetheJSON = new MovieDataParser(s);
            //List<Movie> movieList = new ArrayList<>();

            final ArrayList<Movie> m = parsetheJSON.parseMovies();
            mMovies = m;
            //mMovies = movies; be it a list of movies
            update();
        }
    }


}
