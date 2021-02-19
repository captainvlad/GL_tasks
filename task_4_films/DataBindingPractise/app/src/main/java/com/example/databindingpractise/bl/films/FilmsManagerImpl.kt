package com.example.databindingpractise.bl.films

import android.util.LruCache
import com.example.databindingpractise.bl.tmdb.TmdbManager
import com.example.databindingpractise.model.Film
import com.example.databindingpractise.model.FilmDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class FilmsManagerImpl(
    private val apiManager: TmdbManager,
    private val filmDao: FilmDao
): FilmsManager {
    private val filmCache = LruCache<Long, Film>(100)
    private val expirationTime = TimeUnit.DAYS.toMillis(1)
    private val dispatcher = Dispatchers.Main

    override suspend fun getFilm(filmId: Long): Film {
        return withContext(dispatcher) {
            var film = filmCache.get(filmId)

            if (film == null) {
                film = filmDao.getFilmById(filmId)
            }

            if (film != null && film.expirationDate + expirationTime <= System.currentTimeMillis()) {
                filmCache.put(film.id, film)
            } else {
                film = apiManager.getFilmInfo(filmId)
                filmDao.putFilm(film)
                filmCache.put(film.id, film)
            }

            film
        }
    }

    override suspend fun getFilmsByGenre(genreId: Long, page: Int): List<Film> {
        return apiManager.getFilmsList(genreId, page)
    }

}