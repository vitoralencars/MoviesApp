package com.vitor.moviesapp.ui.activity.favoritemovies

import android.os.Bundle
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.R
import com.vitor.moviesapp.base.BaseActivity
import com.vitor.moviesapp.database.FavoriteMoviesObject
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.ui.adapter.FavoriteMoviesAdapter
import com.vitor.moviesapp.ui.adapter.GenresAdapter
import com.vitor.moviesapp.util.DateUtils
import com.vitor.moviesapp.util.NetworkConstants
import com.vitor.moviesapp.util.loadImage
import kotlinx.android.synthetic.main.activity_favorite_movies.*
import kotlinx.android.synthetic.main.content_favorite_movies.*
import kotlinx.android.synthetic.main.view_empty_list_warning.*
import kotlinx.android.synthetic.main.view_movie_details.*
import org.koin.android.ext.android.inject

class FavoriteMoviesActivity : BaseActivity(), FavoriteMoviesContract.View {

    private val presenter: FavoriteMoviesContract.Presenter by inject()

    private val favoriteMoviesList = ArrayList<Movie>()
    private lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutReference(R.layout.activity_favorite_movies)
        setupRecyclerView()
        setupPresenter()
    }

    private fun setupRecyclerView() {
        favoriteMoviesAdapter = FavoriteMoviesAdapter(this)
        rv_favorite_movies.adapter = favoriteMoviesAdapter
    }

    private fun setupPresenter() {
        presenter.attachView(this)
        presenter.attachDataBase(database)

        presenter.getFavoriteMovies()
    }

    fun removeMovie(movie: Movie){
        presenter.removeFavoriteMovie(movie)
    }

    override fun listFavoriteMovies(movies: List<Movie>) {
        favoriteMoviesList.addAll(movies)
        favoriteMoviesAdapter.updateList(favoriteMoviesList)
    }

    override fun showMovieDetails(movie: Movie){
        iv_poster_details.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
        tv_title_details.text = movie.title
        tv_overview.text = movie.overview
        rating_bar_details.rating = movie.voteAverage/2
        tv_vote_count_details.text = getString(R.string.parentheses_style, movie.voteCount.toString())
        rv_genrers_details.adapter = GenresAdapter(this, presenter.getMovieGenres(movie))
        tv_release_year_details.text = getString(R.string.parentheses_style, DateUtils.getYear(movie.releaseDate))

        iv_favorite_details.visibility = View.GONE

        panel_layout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
    }

    override fun removeFavoriteMovie(movie: Movie) {
        database.favoriteMovieDao().deleteMovie(movie)
        FavoriteMoviesObject.removeMovie(movie)
    }

    override fun reloadFavoriteMoviesList() {
        favoriteMoviesList.clear()
        presenter.getFavoriteMovies()
    }

    override fun showEmptyListWarning() {
        tv_empty_list.visibility = View.VISIBLE
        rv_favorite_movies.visibility = View.GONE
    }

    override fun showList() {
        tv_empty_list.visibility = View.GONE
        rv_favorite_movies.visibility = View.VISIBLE
    }

}
