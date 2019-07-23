package com.vitor.moviesapp.ui.activity.movieslist

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.database.FavoriteMoviesObject
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.service.DiscoverService
import com.vitor.moviesapp.network.service.GenreService
import com.vitor.moviesapp.network.service.SearchService
import com.vitor.moviesapp.util.*
import com.vitor.moviesapp.util.constant.LanguageConstants
import com.vitor.moviesapp.util.constant.NetworkConstants
import com.vitor.moviesapp.util.datautil.GenreUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MoviesListPresenter: MoviesListContract.Presenter {

    private lateinit var view: MoviesListContract.View

    private lateinit var discoverService: DiscoverService
    private lateinit var genreService: GenreService
    private lateinit var searchService: SearchService

    private lateinit var dataBase: AppDataBase

    private val compositeDisposable = CompositeDisposable()

    override fun attachView(view: MoviesListContract.View) {
        this.view = view
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

    override fun attachServices(discoverService: DiscoverService, genreService: GenreService, searchService: SearchService) {
        this.discoverService = discoverService
        this.genreService = genreService
        this.searchService = searchService
    }

    override fun attachDataBase(dataBase: AppDataBase) {
        this.dataBase = dataBase
    }

    override fun getRemoteMovies(context: Context) {
        if(PageObject.hasMorePages()) {
            if (PageObject.currentPage == 1) {
                view.clearRemoteMoviesArray()
                view.showProgressBar()
            }
            compositeDisposable.add(discoverService.getMovies(
                    NetworkConstants.API_KEY,
                    SortObject.currentSortOrder,
                    PageObject.currentPage,
                    LanguageConstants.PT_BR,
                    true,
                    100
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        PageObject.maxPages = it.totalPages
                        view.hideNetworkUnavailableWarning()
                        view.listRemoteMovies(it.results)
                        view.hideProgressBar()

                        setupListActions(it.results.isEmpty())

                    }, {
                        view.hideProgressBar()
                        checkConnectionStatus(context)
                    })
            )
        }
    }

    override fun getFavoriteMovies() {
        compositeDisposable.add(dataBase.favoriteMovieDao().getFavoriteMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                FavoriteMoviesObject.updateMoviesList(it)
                view.reloadMoviesList()
            },{

            })
        )
    }

    override fun getGenres() {
        compositeDisposable.add(genreService.getGenres(NetworkConstants.API_KEY, LanguageConstants.PT_BR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                GenreUtils.genrers = it.genres
            },{

            })
        )
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

    override fun checkPanelState(movie: Movie, panel: SlidingUpPanelLayout) {
        if(panel.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            view.showMovieDetails(movie)
        }else {
            panel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    override fun searchMoviesByQuery(context: Context, query: String) {
        PageObject.resetPage()

        if(PageObject.hasMorePages()){
            if (PageObject.currentPage == 1) {
                view.clearRemoteMoviesArray()
                view.showProgressBar()
            }
            compositeDisposable.add(searchService.searchByQuery(
                    NetworkConstants.API_KEY,
                    LanguageConstants.PT_BR, query,
                    PageObject.currentPage,
                    true
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        PageObject.maxPages = it.totalPages
                        view.hideNetworkUnavailableWarning()
                        view.listRemoteMovies(it.results)
                        view.hideProgressBar()
                        setupListActions(it.results.isEmpty())
                    }, {
                        view.hideProgressBar()
                        checkConnectionStatus(context)
                    })
            )

        }
    }

    override fun clearQueryText(context: Context) {
        PageObject.resetPage()
        getRemoteMovies(context)
    }


    override fun sortMovies(context: Context, spinnerIndex: Int) {
        PageObject.resetPage()

        val sortByOption = when(spinnerIndex){
            SortObject.SortByEnum.POPULARITY.ordinal -> SortObject.SORT_BY_POPULARITY
            SortObject.SortByEnum.RELEASE_DATE.ordinal -> SortObject.SORT_BY_RELEASE_DATE
            SortObject.SortByEnum.AVERAGE.ordinal -> SortObject.SORT_BY_AVERAGE
            SortObject.SortByEnum.VOTE_COUNT.ordinal -> SortObject.SORT_BY_VOTE_COUNT
            else -> SortObject.SORT_BY_POPULARITY
        }

        SortObject.currentSortOrder = sortByOption
        getRemoteMovies(context)
    }

    override fun setFavoriteMovieAction(movie: Movie, favoriteIcon: AppCompatImageView) {
        compositeDisposable.add(dataBase.favoriteMovieDao().getFavoriteMovieById(movie.id)
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
        )
    }

    override fun checkIsFavoriteMovie(movie: Movie, favoriteIcon: AppCompatImageView) {
        compositeDisposable.add(dataBase.favoriteMovieDao().getFavoriteMovieById(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.checkFavoriteIcon(favoriteIcon)
            },{
                view.unCheckFavoriteIcon(favoriteIcon)
            })
        )
    }

    override fun checkListScroll(context: Context, canScroll: Boolean) {
        if(!canScroll){
            PageObject.setNextPage()
            getRemoteMovies(context)
        }
    }

    private fun setupListActions(emptyList: Boolean){
        if(emptyList){
            view.showEmptyListWarning()
        }else{
            view.showList()
        }
    }

    private fun checkConnectionStatus(context: Context){
        if(NetworkStatusObject.isNetworkAvailable(context)) {
            view.hideNetworkUnavailableWarning()
            view.showEmptyListWarning()
        }else{
            view.showNetworkUnavailableWarning()
        }
    }
}