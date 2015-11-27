package com.formovies.shelleywu.rottenegg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shelleywu on 11/26/15.
 */
public class Movie implements Parcelable {

    String title;
    String overview;
    String poster;
    String rating;
    String date;
    String id;

    public Movie(String title, String overview, String poster, String rating, String date, String id) {
        this.title = title;
        this.overview = overview;
        this.poster = poster;
        this.rating = rating;
        this.date = date;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        poster = in.readString();
        rating = in.readString();
        date = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(rating);
        dest.writeString(date);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}