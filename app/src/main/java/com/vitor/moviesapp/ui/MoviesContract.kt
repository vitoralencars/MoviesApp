package com.vitor.moviesapp.ui

import android.support.v7.widget.AppCompatImageView
import com.vitor.moviesapp.base.BaseContract
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.DiscoverService
import com.vitor.moviesapp.network.GenreService

class MoviesContract: BaseContract() {

    interface View: BaseView{
        fun listRemoteMovies(movies: List<Movie>)
        fun clearRemoteMoviesArray()
        fun reloadMoviesList()
    }

    interface Presenter: BasePresenter<View>{
        fun attachServices(discoverService: DiscoverService, genreService: GenreService)
        fun attachDataBase(dataBase: AppDataBase)
        fun getRemoteMovies()
        fun getFavoriteMovies()
        fun getGenres()
        fun getMovieGenres(movie: Movie): List<Genre>
        fun sortMovies(spinnerIndex: Int)
        fun setFavoriteMovieAction(movie: Movie, favoriteIcon: AppCompatImageView)
        fun checkIsFavoriteMovie(movie: Movie, favoriteIcon: AppCompatImageView)
        fun checkListScroll(canScroll: Boolean)
    }

}