package com.vitor.moviesapp.ui

import android.annotation.SuppressLint
import android.support.v7.widget.AppCompatImageView
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.database.FavoriteMoviesObject
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.service.DiscoverService
import com.vitor.moviesapp.network.service.GenreService
import com.vitor.moviesapp.network.service.SearchService
import com.vitor.moviesapp.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MoviesPresenter: MoviesContract.Presenter {

    lateinit var view: MoviesContract.View

    lateinit var discoverService: DiscoverService
    lateinit var genreService: GenreService
    lateinit var searchService: SearchService

    lateinit var dataBase: AppDataBase

    override fun attachView(view: MoviesContract.View) {
        this.view = view
    }

    override fun attachServices(discoverService: DiscoverService, genreService: GenreService, searchService: SearchService) {
        this.discoverService = discoverService
        this.genreService = genreService
        this.searchService = searchService
    }

    override fun attachDataBase(dataBase: AppDataBase) {
        this.dataBase = dataBase
    }

    override fun getRemoteMovies() {
        if(PageObject.hasMorePages()) {
            if (PageObject.currentPage == 1) {
                view.clearRemoteMoviesArray()
                view.showProgressBar()
            }
            discoverService.getMovies(
                NetworkConstants.API_KEY,
                SortObject.currentSortOrder,
                PageObject.currentPage,
                LanguageConstants.PT_BR,
                true,
                500
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    PageObject.maxPages = it.totalPages
                    view.listRemoteMovies(it.results)
                    view.hideProgressBar()
                }, {
                    view.hideProgressBar()
                })
        }
    }

    override fun getFavoriteMovies() {
        dataBase.favoriteMovieDao().getFavoriteMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                FavoriteMoviesObject.updateMoviesList(it)
                view.reloadMoviesList()
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

    override fun getMovieGenres(movie: Movie): ArrayList<Genre>{
        val genresList = ArrayList<Genre>()

        GenreUtils.genrers.forEach{
            if(it.id in movie.genreIds){
                genresList.add(it)
            }
        }

        return genresList
    }

    override fun searchMoviesByQuery(query: String) {
        PageObject.resetPage()

        if(PageObject.hasMorePages()){
            if (PageObject.currentPage == 1) {
                view.clearRemoteMoviesArray()
                view.showProgressBar()
            }
            searchService.searchByQuery(
                NetworkConstants.API_KEY,
                LanguageConstants.PT_BR, query,
                PageObject.currentPage,
                true
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    PageObject.maxPages = it.totalPages
                    view.listRemoteMovies(it.results)
                    view.hideProgressBar()
                }, {
                    view.hideProgressBar()
                })

        }
    }

    override fun clearQueryText() {
        PageObject.resetPage()
        getRemoteMovies()
    }


    override fun sortMovies(spinnerIndex: Int) {
        PageObject.resetPage()

        val sortByOption = when(spinnerIndex){
            SortObject.SortByEnum.POPULARITY.ordinal -> SortObject.SORT_BY_POPULARITY
            SortObject.SortByEnum.RELEASE_DATE.ordinal -> SortObject.SORT_BY_RELEASE_DATE
            SortObject.SortByEnum.AVERAGE.ordinal -> SortObject.SORT_BY_AVERAGE
            SortObject.SortByEnum.VOTE_COUNT.ordinal -> SortObject.SORT_BY_VOTE_COUNT
            else -> SortObject.SORT_BY_POPULARITY
        }

        SortObject.currentSortOrder = sortByOption
        getRemoteMovies()
    }

    override fun setFavoriteMovieAction(movie: Movie, favoriteIcon: AppCompatImageView) {
        dataBase.favoriteMovieDao().getFavoriteMovieById(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.removeFavoriteMovie(movie)
                view.unCheckFavoriteIcon(favoriteIcon)
                view.reloadMoviesList()
            },{
                view.insertFavoriteMovie(movie)
                view.checkFavoriteIcon(favoriteIcon)
                view.reloadMoviesList()
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

    override fun checkListScroll(canScroll: Boolean) {
        if(!canScroll){
            PageObject.setNextPage()
            getRemoteMovies()
        }
    }
}