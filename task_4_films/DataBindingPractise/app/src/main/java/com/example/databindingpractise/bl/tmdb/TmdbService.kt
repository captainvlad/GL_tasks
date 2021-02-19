package com.example.databindingpractise.bl.tmdb

import com.example.databindingpractise.model.responseModels.FilmResponse
import com.example.databindingpractise.model.responseModels.GenresResponse
import com.example.databindingpractise.model.responseModels.FilmsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {
    @GET("3/genre/movie/list")
    suspend fun getGenresList(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): GenresResponse

    @GET("3/movie/{movie_id}")
    fun getFilm(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<FilmResponse>

    @GET("3/discover/movie")
    fun getFilmsByGenre(
        @Query("api_key") key: String,
        @Query("page") pageNumber: Int,
        @Query("with_genres") genreId: Long,
        @Query("sort_by") sortRegime: String = "popularity.desc"
    ): Call<FilmsResponse>
}