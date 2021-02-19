package com.example.databindingpractise.model.responseModels

data class FilmsResponse(
    val results: List<FilmResponse> = emptyList()
)