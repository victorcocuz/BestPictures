package com.example.victor.bestpictures.Model;


import java.util.List;

/**
 * Created by Victor on 3/10/2018.
 */

public class MovieItem {

    private int movieId;
    private String movieBackdrop;
    private int movieBudget;
    private List<String> movieGenres;
    private String movieOverview;
    private String moviePopularity;
    private String moviePoster;
    private String movieReleaseDate;
    private int movieRevenue;
    private int movieRuntime;
    private String movieTitle;
    private String movieVoteAverage;
    private List<CastMember> movieCastMembers;
    private String movieDirector;
    private List<CastMember> movieCrewMembers;
    private List<ReviewItem> movieReviewItems;


    public MovieItem(int movieId,
                     String movieBackdrop,
                     int movieBudget,
                     List<String> movieGenres,
                     String movieOverview,
                     String moviePopularity,
                     String moviePoster,
                     String movieReleaseDate,
                     int movieRevenue,
                     int movieRuntime,
                     String movieTitle,
                     String movieVoteAverage,
                     List<CastMember> movieCastMembers,
                     String movieDirector,
                     List<CastMember> movieCrewMembers,
                     List<ReviewItem> movieReviewItems) {

        this.movieId = movieId;
        this.movieBackdrop = movieBackdrop;
        this.movieBudget = movieBudget;
        this.movieGenres = movieGenres;
        this.movieOverview = movieOverview;
        this.moviePopularity = moviePopularity;
        this.moviePoster = moviePoster;
        this.movieReleaseDate = movieReleaseDate;
        this.movieRevenue = movieRevenue;
        this.movieRuntime = movieRuntime;
        this.movieTitle = movieTitle;
        this.movieVoteAverage = movieVoteAverage;
        this.movieCastMembers = movieCastMembers;
        this.movieDirector = movieDirector;
        this.movieCrewMembers = movieCrewMembers;
        this.movieReviewItems = movieReviewItems;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieBackdrop() {
        return movieBackdrop;
    }

    public int getMovieBudget() {
        return movieBudget;
    }

    public List<String> getMovieGenres() {
        return movieGenres;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMoviePopularity() {
        return moviePopularity;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public int getMovieRevenue() {
        return movieRevenue;
    }

    public int getMovieRuntime() {
        return movieRuntime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public List<CastMember> getMovieCastMembers() {return movieCastMembers;}

    public String getMovieDirector() {return movieDirector;}

    public List<CastMember> getMovieCrewMembers() {return movieCrewMembers;}

    public List<ReviewItem> getMovieReviewItems() {return movieReviewItems;}

}
