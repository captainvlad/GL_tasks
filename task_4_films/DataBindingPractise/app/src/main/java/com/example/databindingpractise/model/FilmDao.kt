package com.example.databindingpractise.model

import androidx.room.*

@Dao
abstract class FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putFilm(film: Film): Long

    @Query("SELECT * FROM films WHERE id == :id")
    abstract suspend fun getFilmById(id: Long): Film?

    @Delete
    abstract suspend fun deleteFilm(film: Film)
}