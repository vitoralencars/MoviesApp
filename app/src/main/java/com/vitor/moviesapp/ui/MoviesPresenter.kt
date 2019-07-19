package com.vitor.moviesapp.ui

import android.annotation.SuppressLint
import android.support.v7.widget.AppCompatImageView
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.DiscoverService
import com.vitor.moviesapp.network.GenreService
import com.vitor.moviesapp.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MoviesPresenter: MoviesContract.Presenter {

    lateinit var view: MoviesContract.View

    lateinit var discoverService: DiscoverService
    lateinit var genreService: GenreService

    lateinit var dataBase: AppDataBase

    override fun attachView(view: MoviesContract.View) {
        this.view = view
    }

    override fun attachServices(discoverService: DiscoverService, genreService: GenreService) {
        this.discoverService = discoverService
        this.genreService = genreService
    }

    override fun attachDataBase(dataBase: AppDataBase) {
        this.dataBase = dataBase
    }

    override fun getRemoteMovies(sortBy: String, page: Int) {
        if(page == 1){
            view.clearRemoteMoviesArray()
            view.showProgressBar()
        }
        discoverService.getMovies(
            NetworkConstants.API_KEY,
            sortBy,
            page,
            LanguageConstants.PT_BR,
            true,
            500)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.listRemoteMovies(it.results)
                view.hideProgressBar()
            },{
                view.hideProgressBar()
            })
    }

    override fun getFavoriteMovies() {
        dataBase.favoriteMovieDao().getFavoriteMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                view.updateFavoriteMoviesList(it)
            },{

            })
    }

    override fun getGenres() {
        genreService.getGenres(NetworkConstants.API_KEY, LanguageConstants.PT_BR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                GenreUtils.genrers = it.genres
            },{
                print(it)
            })
    }

    override fun getMovieGenres(movie: Movie): List<Genre>{
        val genresList = ArrayList<Genre>()

        GenreUtils.genrers.forEach{
            /*if(movie.genreIds.contains(it.id)){
                genresList.add(it)
            }*/
        }

        return genresList
    }

    override fun sortMovies(spinnerIndex: Int) {

        val sortByOption = when(spinnerIndex){
            SortUtils.SortByEnum.POPULARITY.ordinal -> SortUtils.SORT_BY_POPULARITY
            SortUtils.SortByEnum.RELEASE_DATE.ordinal -> SortUtils.SORT_BY_RELEASE_DATE
            SortUtils.SortByEnum.AVERAGE.ordinal -> SortUtils.SORT_BY_AVERAGE
            SortUtils.SortByEnum.VOTE_COUNT.ordinal -> SortUtils.SORT_BY_VOTE_COUNT
            else -> SortUtils.SORT_BY_POPULARITY
        }

        getRemoteMovies(sortByOption, 1)
    }

    override fun setFavoriteMovieAction(movie: Movie, favoriteIcon: AppCompatImageView) {
        dataBase.favoriteMovieDao().getFavoriteMovieById(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.removeFavoriteMovie(movie)
                view.unCheckFavoriteIcon(favoriteIcon)
            },{
                view.insertFavoriteMovie(movie)
                view.checkFavoriteIcon(favoriteIcon)
            })
    }

    override fun checkIsFavoriteMovie(movie: Movie, favoriteIcon: AppCompatImageView) {
        dataBase.favoriteMovieDao().getFavoriteMovieById(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.checkFavoriteIcon(favoriteIcon)
            },{
                view.unCheckFavoriteIcon(favoriteIcon)
            })
    }

}