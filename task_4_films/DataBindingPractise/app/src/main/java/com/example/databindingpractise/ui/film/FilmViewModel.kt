package com.example.databindingpractise.ui.film

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databindingpractise.bl.films.FilmsManager
import com.example.databindingpractise.model.Film
import kotlinx.coroutines.launch

class FilmViewModel(
    private val filmId: Long,
    private val filmsManager: FilmsManager
): ViewModel() {
    val filmLiveData = MutableLiveData<Film>()

    init {
        viewModelScope.launch {
            filmLiveData.value = filmsManager.getFilm(filmId)
        }
    }

}