package com.example.databindingpractise.bl.genres

import com.example.databindingpractise.bl.tmdb.TmdbManager
import com.example.databindingpractise.model.Genre
import com.example.databindingpractise.model.GenreDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class GenresManagerImpl(
    private val apiManager: TmdbManager,
    private val genreDao: GenreDao
) : GenresManager {
    private var genresListCache = emptyList<Genre>()
    private val expirationTime = TimeUnit.DAYS.toMillis(1)

    override suspend fun getAllGenres(): List<Genre> {
        return withContext(Dispatchers.IO) {
            if (genresListCache.isEmpty() || genresListCache.any
                { it.expirationDate + expirationTime > System.currentTimeMillis() }) {
                genresListCache = genreDao.getAllGenres()
            }

            if (genresListCache.isEmpty() || genresListCache.any
                { it.expirationDate + expirationTime > System.currentTimeMillis() }) {
                genresListCache = apiManager.getGenres()
                genresListCache.filter { it.id != -1L }.forEach { genreDao.putGenre(it) }
            }
            genresListCache
        }
    }
}