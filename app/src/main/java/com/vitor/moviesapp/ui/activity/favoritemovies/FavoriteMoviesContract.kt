package com.vitor.moviesapp.ui.activity.favoritemovies

import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.base.BaseContract
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie

class FavoriteMoviesContract: BaseContract() {

    interface View: BaseView{
        fun listFavoriteMovies(movies: List<Movie>)
        fun showMovieDetails(movie: Movie)
        fun reloadFavoriteMoviesList()
        fun removeFavoriteMovie(movie: Movie)
        fun showEmptyListWarning()
        fun showList()
    }

    interface Presenter: BasePresenter<View>{
        fun attachDataBase(dataBase: AppDataBase)
        fun getFavoriteMovies()
        fun removeFavoriteMovie(movie: Movie)
        fun getMovieGenres(movie: Movie): List<Genre>
        fun checkPanelState(movie: Movie, panel: SlidingUpPanelLayout)
    }

}