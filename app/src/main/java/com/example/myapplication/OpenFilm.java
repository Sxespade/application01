package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenFilm {
    @GET("/")
    Call<FilmRequest> loadFilmName( @Query("t")String m, @Query("apikey") String keyApi);

    @GET("/")
    Call<FilmRequest> loadFilmYear( @Query("Year")String m, @Query("apikey") String keyApi);
}

