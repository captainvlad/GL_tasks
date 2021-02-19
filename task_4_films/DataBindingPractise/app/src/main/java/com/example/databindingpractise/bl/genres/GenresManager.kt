package com.example.databindingpractise.bl.genres

import com.example.databindingpractise.model.Genre

interface GenresManager {
    suspend fun getAllGenres(): List<Genre>
}