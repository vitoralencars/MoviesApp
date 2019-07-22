package com.vitor.moviesapp.ui.activity.movieslist

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.base.BaseContract
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.service.DiscoverService
import com.vitor.moviesapp.network.service.GenreService
import com.vitor.moviesapp.network.service.SearchService

class MoviesListContract: BaseContract() {

    interface View: BaseView{
        fun listRemoteMovies(movies: List<Movie>)
        fun showMovieDetails(movie: Movie)
        fun clearRemoteMoviesArray()
        fun reloadMoviesList()
        fun insertFavoriteMovie(movie: Movie)
        fun removeFavoriteMovie(movie: Movie)
        fun checkFavoriteIcon(favoriteIcon: AppCompatImageView)
        fun unCheckFavoriteIcon(favoriteIcon: AppCompatImageView)
        fun showEmptyListWarning()
        fun showList()
        fun showNetworkUnavailableWarning()
        fun hideNetworkUnavailableWarning()
    }

    interface Presenter: BasePresenter<View>{
        fun attachServices(discoverService: DiscoverService, genreService: GenreService, searchService: SearchService)
        fun attachDataBase(dataBase: AppDataBase)
        fun getRemoteMovies(context: Context)
        fun getFavoriteMovies()
        fun getGenres()
        fun getMovieGenres(movie: Movie): List<Genre>
        fun checkPanelState(movie: Movie, panel: SlidingUpPanelLayout)
        fun searchMoviesByQuery(context: Context, query: String)
        fun clearQueryText(context: Context)
        fun sortMovies(context: Context, spinnerIndex: Int)
        fun setFavoriteMovieAction(movie: Movie, favoriteIcon: AppCompatImageView)
        fun checkIsFavoriteMovie(movie: Movie, favoriteIcon: AppCompatImageView)
        fun checkListScroll(context: Context, canScroll: Boolean)
    }

}