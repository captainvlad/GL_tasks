package com.example.databindingpractise.bl.tmdb

import com.example.databindingpractise.model.Film
import com.example.databindingpractise.model.Genre

interface TmdbManager {
    suspend fun getGenres(): List<Genre>
    suspend fun getFilmsList(genreId: Long, page: Int = 1): List<Film>
    suspend fun getFilmInfo(id: Long): Film
}