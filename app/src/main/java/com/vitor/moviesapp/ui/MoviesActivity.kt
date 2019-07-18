package com.vitor.moviesapp.ui

import android.os.Bundle
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.R
import com.vitor.moviesapp.base.BaseActivity
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.DiscoverService
import com.vitor.moviesapp.network.GenreService
import com.vitor.moviesapp.ui.adapter.GenresAdapter
import com.vitor.moviesapp.ui.adapter.MoviesAdapter
import com.vitor.moviesapp.util.NetworkConstants
import com.vitor.moviesapp.util.RecyclerViewOnClickListener
import com.vitor.moviesapp.util.loadImage
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.view_movie_details.*
import org.koin.android.ext.android.inject

class MoviesActivity : BaseActivity(), MoviesContract.View, RecyclerViewOnClickListener{

    private val presenter: MoviesContract.Presenter by inject()
    private val discoverService: DiscoverService by inject()
    private val genreService: GenreService by inject()

    private val moviesList = ArrayList<Movie>()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutReference(R.layout.activity_movies)
        setUpRecyclerView()
        setUpPresenter()

        presenter.getMovies()
        presenter.getGenres()
    }

    private fun setUpRecyclerView(){
        moviesAdapter = MoviesAdapter(this)
        rv_movies.adapter = moviesAdapter
    }

    private fun setUpPresenter(){
        presenter.attachView(this)
        presenter.attachServices(discoverService, genreService)
    }

    private fun showMovieDetails(movie: Movie){
        iv_poster_details.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
        tv_title_details.text = movie.title
        tv_overview.text = movie.overview
        rating_bar_details.rating = movie.voteAverage/2
        tv_vote_count_details.text = "(${movie.voteCount})"
        rv_genrers.adapter = GenresAdapter(this, presenter.getMovieGenres(movie))

        panel_layout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
    }

    override fun listMovies(movies: List<Movie>) {
        moviesList.addAll(movies)
        moviesAdapter.updateList(movies)
    }

    override fun onItemClick(position: Int) {
        showMovieDetails(moviesList[position])
    }
}
