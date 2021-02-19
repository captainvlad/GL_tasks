package com.example.databindingpractise.model.responseModels

import com.google.gson.annotations.SerializedName

data class FilmResponse (
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("budget")
    val budget: Long? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("homepage")
    val homepage: String? = null,
    @SerializedName("adult")
    val adult: Boolean? = null,
    @SerializedName("tagline")
    val tagline: String? = null,
    @SerializedName("vote_average")
    val voteAverage: Float? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null
)