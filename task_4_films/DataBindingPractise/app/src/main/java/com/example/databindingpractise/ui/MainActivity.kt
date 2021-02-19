package com.example.databindingpractise.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.databindingpractise.R
import com.example.databindingpractise.databinding.ActivityMainBinding
import com.example.databindingpractise.ui.film.FilmFragment
import com.example.databindingpractise.ui.filmList.FilmListFragment
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), FilmListFragment.FilmChosen {
    private val viewModel by viewModel<MainViewModel>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        binding.navView.setNavigationItemSelectedListener {
            onGenreMenuItemClicked(it)
        }

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbarMain,
            R.string.ask_about,
            R.string.ask_about,
        )
        binding.drawerLayout.addDrawerListener(toggle)

        viewModel.genresLiveData.observe(this) {
            it.forEach { binding.navView
                .menu
                .add(0, it.id.toInt(), 0, it.name)
            }
        }

        viewModel.getAllGenres()
        drawerLayout = binding.drawerLayout
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun onGenreMenuItemClicked(it: MenuItem): Boolean {
        if (it.itemId != -1) {
            drawerLayout.closeDrawer(GravityCompat.START)

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.short_film_layout, FilmListFragment.newInstance(it.itemId.toLong()))
                .commit()
        }
        return true
    }

    override fun onFilmClicked(filmId: Long) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("name")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.short_film_layout, FilmFragment.newInstance(filmId))
            .commit()
    }
}