package com.example.databindingpractise.bl.tmdb

import android.util.Log
import com.example.databindingpractise.model.*
import com.example.databindingpractise.model.responseModels.FilmResponse
import com.example.databindingpractise.model.responseModels.FilmsResponse
import com.example.databindingpractise.model.responseModels.GenreResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class TmdbManagerImpl (
    private val apiKey: String = "5827cfca0660a99efc6f08f5664d5858",
    private val apiLanguage: String = "en-US",
    basicUrl: String = "https://api.themoviedb.org/"
): TmdbManager {
    private val retrofit = Retrofit.Builder()
        .baseUrl(basicUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(TmdbService::class.java)

    override suspend fun getGenres(): List<Genre> {
        return try {
            service.getGenresList(key = apiKey, language = apiLanguage)
                .genres
                .map { responseToGenre((it)) }

        } catch (e: Exception) {
            listOf(Genre())
        }
    }

    override suspend fun getFilmsList(genreId: Long, page: Int): List<Film> {
        return withContext(Dispatchers.IO) {
            try {
                val responseBody = service.getFilmsByGenre(
                    key = apiKey,
                    pageNumber = page + 1,
                    genreId = genreId
                ).execute()
                 .body() ?: FilmsResponse()

                responseBody.results.map { responseToFilm(it) }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getFilmInfo(id: Long): Film {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getFilm(
                    movieId = id,
                    apiKey = apiKey,
                    language = apiLanguage
                ).execute()
                    .body() ?: FilmResponse()

                responseToFilm(response)
            } catch (e: Exception) {
                Film()
            }
        }
    }

    private fun responseToFilm(response: FilmResponse): Film {
        return Film(
            id = response.id ?: 0L,
            budget = response.budget ?: 0L,
            status = response.status ?: "-",
            title = response.title ?: "-",
            homepage = response.homepage ?: "-",
            adult = response.adult ?: false,
            tagline = response.tagline ?: "-",
            voteAverage = response.voteAverage ?: 0F,
            releaseDate = response.releaseDate ?: "-",
            overview = response.overview ?: "-",
            posterPath = response.posterPath ?: "-"
        )
    }

    private fun responseToGenre(response: GenreResponse): Genre {
        return Genre (
            response.id,
            response.name
        )
    }
}

