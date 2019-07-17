package com.vitor.moviesapp.ui

import android.os.Bundle
import com.vitor.moviesapp.R
import com.vitor.moviesapp.base.BaseActivity
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.NetworkService
import com.vitor.moviesapp.ui.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.content_movies.*
import org.koin.android.ext.android.inject

class MoviesActivity : BaseActivity(), MoviesContract.View{

    private val presenter: MoviesContract.Presenter by inject()
    private val networkService: NetworkService by inject()

    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        setUpRecyclerView()

        presenter.attachView(this)
        presenter.attachNetworkService(networkService)
        presenter.getMovies()
    }

    override fun listMovies(movies: List<Movie>) {
        moviesAdapter.updateList(movies)
    }

    fun setUpRecyclerView(){
        moviesAdapter = MoviesAdapter(this)
        rv_movies.adapter = moviesAdapter
    }
}
