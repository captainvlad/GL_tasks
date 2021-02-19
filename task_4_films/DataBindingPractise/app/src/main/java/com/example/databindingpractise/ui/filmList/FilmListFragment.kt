package com.example.databindingpractise.ui.filmList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingpractise.databinding.FilmLayoutBinding
import com.example.databindingpractise.databinding.FilmListFragmentBinding
import com.example.databindingpractise.ui.BindingFragment
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel


class FilmListFragment: BindingFragment<FilmListFragmentBinding>(FilmListFragmentBinding::inflate) {
    companion object {
        fun newInstance(genreId: Long) = FilmListFragment().apply {
            arguments = bundleOf("genreId" to genreId)
        }
    }

    interface FilmChosen {
        fun onFilmClicked(filmId: Long)
    }

    private lateinit var filmsAdapter: FilmsAdapter

    private val viewModel by viewModel<FilmListViewModel>{
        parametersOf(requireArguments().getLong("genreId"))
    }

    override fun onBindingCreated(binding: FilmListFragmentBinding, savedInstanceState: Bundle?) {
        filmsAdapter = FilmsAdapter(
            viewLifecycleOwner,
            viewModel.adapterFilmsData,
            viewModel.noItemsLeftLiveData,
            requireContext(),
            { (activity as FilmChosen).onFilmClicked(it) },
            { viewModel.loadFilms(it) }
        )

//        viewModel.adapterFilmsData.observe(viewLifecycleOwner) {
//            filmsAdapter.items = it
//        }
//
//        viewModel.noItemsLeftLiveData.observe(viewLifecycleOwner) {
//            filmsAdapter.noItemsLeft = it
//        }

        binding.shortFilmRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.shortFilmRecycler.adapter = filmsAdapter
    }
}