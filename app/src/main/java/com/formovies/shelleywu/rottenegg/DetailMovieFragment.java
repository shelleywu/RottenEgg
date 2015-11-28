package com.formovies.shelleywu.rottenegg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment {
    private static final String ARGS_MOVIE = "movie";

    Movie mMovie;
    private String mImageUrl = "http://image.tmdb.org/t/p/";
    private String mImageWidth = "w500";

    TextView title, overview, reviews, trailerText;
    ImageView poster, trailerImage, fave;

    private String REVIEWS_ID = "/reviews";

    private static final String TAG_AUTHOR = "author";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_RESULTS = "results";

    public static final String PREFS = "Favorites";

    int position;

    public DetailMovieFragment() {
    }

    public static DetailMovieFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_MOVIE, movie);
        DetailMovieFragment fragment = new DetailMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = getArguments().getParcelable(ARGS_MOVIE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        //Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);


        title = (TextView)rootView.findViewById(R.id.movie_title);
        overview = (TextView)rootView.findViewById(R.id.movie_overview);
        poster = (ImageView)rootView.findViewById(R.id.movie_poster);
        reviews = (TextView)rootView.findViewById(R.id.movie_reviews);
        trailerText = (TextView)rootView.findViewById(R.id.movie_trailer);
        trailerImage = (ImageView)rootView.findViewById(R.id.play_button);
        fave = (ImageView)rootView.findViewById(R.id.favorite);

        position = Integer.parseInt(mMovie.getId());

        ArrayList<Movie> movies = MovieDataParser.movies;
        Bundle b = getActivity().getIntent().getExtras();
        if(b != null && b.containsKey("position")) {
            position = b.getInt("position");
            mMovie = movies.get(position);
        }

        if(b != null && b.containsKey("positionfave"))
        {
            ArrayList<Movie> favemovies = MainActivityFragment.favorites;
            mMovie = favemovies.get(b.getInt("positionfave"));
            fave.setImageResource(R.drawable.happy);
        }
        else{
            fave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fave.setImageResource(R.drawable.happy);
                    SharedPreferences sp = getActivity().getSharedPreferences(PREFS, 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("movie", position);
                    editor.commit();
                }
            });
        }



        trailerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                intent.putExtra("id", mMovie.getId());
                startActivity(intent);
            }
        });

        trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                intent.putExtra("id", mMovie.getId());
                startActivity(intent);
            }
        });

        Picasso.with(getActivity())
                .load(mImageUrl + mImageWidth + "/" + mMovie.getPoster())
                .into(poster);

        /**Picasso
         .with(getApplicationContext())
         .load("http://image.tmdb.org/t/p/w500/" + m.getPoster())
         .into(poster);**/

        title.setText(mMovie.getTitle());
        overview.setText("Average Rating: " + mMovie.getRating() + "\n" +
                "Release Date: " + mMovie.getDate() + "\n" +
                "Plot Synopsis: " + mMovie.getOverview());

        new JSONTask().execute(Config.REVIEWS + mMovie.getId() + REVIEWS_ID + Config.MOVIE_API_KEY);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //if (id==android.R.id.home) {
            getActivity().finish();
        //}
        return super.onOptionsItemSelected(item);
    }

    public class JSONTask extends AsyncTask<String, String, String> {

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
            StringBuffer sb = new StringBuffer();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray originalTitleArray = jsonObject.getJSONArray(TAG_RESULTS);

                for(int i = 0; i < originalTitleArray.length(); i++)
                {
                    JSONObject moviereviews = originalTitleArray.getJSONObject(i);
                    String author = "";
                    String content = "";

                    author = moviereviews.getString(TAG_AUTHOR);
                    content = moviereviews.getString(TAG_CONTENT);

                    sb.append("\nAuthor: " + author + "\nContent: " + content);
                }

                reviews.setText(sb.toString());
            } catch (JSONException e) {
                Log.e("JSONException", "bad string");
            }

        }
    }
}
