package com.vitor.moviesapp.ui

import com.vitor.moviesapp.base.BaseContract
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.DiscoverService
import com.vitor.moviesapp.network.GenreService

class MoviesContract: BaseContract() {

    interface View: BaseView{
        fun listMovies(movies: List<Movie>)
        fun clearMoviesArray()
    }

    interface Presenter: BasePresenter<View>{
        fun attachServices(discoverService: DiscoverService, genreService: GenreService)
        fun getMovies(sortBy: String, page: Int)
        fun getGenres()
        fun getMovieGenres(movie: Movie): List<Genre>
        fun sortMovies(spinnerIndex: Int)
    }

}