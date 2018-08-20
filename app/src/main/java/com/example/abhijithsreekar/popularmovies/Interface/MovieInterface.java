package com.example.abhijithsreekar.popularmovies.Interface;

import com.example.abhijithsreekar.popularmovies.Models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieInterface {

    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);
}

