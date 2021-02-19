package com.example.databindingpractise.bl.films

import com.example.databindingpractise.model.Film

interface FilmsManager {
    suspend fun getFilm(filmId: Long): Film
    suspend fun getFilmsByGenre(genreId: Long, page: Int = 1): List<Film>
}