package com.example.databindingpractise.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putGenre(genre: Genre): Long

    @Delete
    abstract suspend fun deleteGenre(genre: Genre)

    @Query("SELECT * FROM genres")
    abstract fun trackAllGenres(): Flow<List<Genre>>

    @Query("SELECT * FROM genres")
    abstract fun getAllGenres(): List<Genre>

    @Query("DELETE FROM genres")
    abstract fun deleteAllGenres()
}