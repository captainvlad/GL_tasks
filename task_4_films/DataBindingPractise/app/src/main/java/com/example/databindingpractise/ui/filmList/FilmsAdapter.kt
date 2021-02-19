package com.example.databindingpractise.ui.filmList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingpractise.databinding.FilmItemBinding
import com.example.databindingpractise.databinding.ProgressLayoutBinding
import com.example.databindingpractise.model.Film
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FilmsAdapter(
    lifecycleOwner: LifecycleOwner,
    filmsLiveData: MutableLiveData<ArrayList<Film>>,
    noItemsLeftLiveData: MutableLiveData<Boolean>,
    private val context: Context,
    private val listener: (Long) -> Unit,
    private val loadMoreFilms: suspend (Int) -> Unit
) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    companion object {
        private const val FILM = 0
        private const val PROGRESSBAR = 1
    }

    private var currentPage = -1
    private var job: Job? = null
    private val dispatcher = Dispatchers.Main
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    var noItemsLeft = false

    var items = ArrayList<Film>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)

        filmsLiveData.observe(lifecycleOwner) {
            items = it
        }

        noItemsLeftLiveData.observe(lifecycleOwner) {
            noItemsLeft = it
        }
    }

    override fun getItemCount(): Int {
        if (items.isEmpty()) {
            loadMoreItems()
        }
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.lastIndex && !noItemsLeft) {
            PROGRESSBAR
        } else {
            FILM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)

        return if (viewType == PROGRESSBAR) {
            val binding = ProgressLayoutBinding.inflate(inflater, parent, false)
            ProgressBarViewHolder(binding)
        } else {
            val binding = FilmItemBinding.inflate(inflater, parent, false)
            FilmViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == items.lastIndex) {
            loadMoreItems()
        }
        holder.bind(items[position])
    }

    private fun loadMoreItems() {
        if (noItemsLeft || job?.isActive == true) {
            return
        }

        job = scope.launch(dispatcher) {
            if (!noItemsLeft) {
            loadMoreFilms(++currentPage)
            }
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Film)
    }

    inner class FilmViewHolder(
        private val binding: FilmItemBinding
    ) : ViewHolder(binding.root) {

        init {
            binding.viewHolder = this
        }

        override fun bind(item: Film) {
            binding.film = item
        }

        fun onItemClicked(filmId: Long) {
            listener(filmId)
        }
    }

    inner class ProgressBarViewHolder(
        binding: ProgressLayoutBinding
    ) : ViewHolder(binding.root) {
        override fun bind(item: Film) {}
    }
}