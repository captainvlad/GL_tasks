package com.example.databindingpractise.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class Film(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "budget")
    val budget: Long = 0,
    @ColumnInfo(name = "countryProducer")
    val status: String = "-",
    @ColumnInfo(name = "title")
    val title: String = "Error while connecting to the server",
    @ColumnInfo(name = "homepage")
    val homepage: String = "-",
    @ColumnInfo(name = "adult")
    val adult: Boolean = false,
    @ColumnInfo(name = "tagline")
    val tagline: String = "-",
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Float = 0F,
    @ColumnInfo(name = "releaseDate")
    val releaseDate: String = "-",
    @ColumnInfo(name = "overView")
    val overview: String = "-",
    @ColumnInfo(name = "posterPath")
    val posterPath: String = "-",
    @ColumnInfo(name = "expirationDate")
    val expirationDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "basicPosterUrl")
    val basicPosterUrl: String = "https://image.tmdb.org/t/p/w500"
)