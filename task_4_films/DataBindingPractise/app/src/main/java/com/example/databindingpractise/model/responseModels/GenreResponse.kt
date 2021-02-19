package com.example.databindingpractise.model.responseModels

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("id")
    val id: Long = -1L,
    @SerializedName("name")
    val name: String = "Sorry, error with server connection",
)