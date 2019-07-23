package com.vitor.moviesapp.ui.activity.favoritemovies

import android.annotation.SuppressLint
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.util.datautil.GenreUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class FavoriteMoviesPresenter: FavoriteMoviesContract.Presenter {

    lateinit var view: FavoriteMoviesContract.View

    lateinit var dataBase: AppDataBase

    private val compositeDisposable = CompositeDisposable()

    override fun attachView(view: FavoriteMoviesContract.View) {
        this.view = view
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

    override fun attachDataBase(dataBase: AppDataBase) {
        this.dataBase = dataBase
    }

    override fun getFavoriteMovies() {
        view.showProgressBar()
        compositeDisposable.add(dataBase.favoriteMovieDao().getFavoriteMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                view.listFavoriteMovies(it)
                view.hideProgressBar()

                if(it.isEmpty()){
                    view.showEmptyListWarning()
                }else{
                    view.showList()
                }

            },{
                view.hideProgressBar()
                view.showEmptyListWarning()
            })
        )
    }

    override fun getMovieGenres(movie: Movie): List<Genre> {
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

    override fun removeFavoriteMovie(movie: Movie) {
        view.removeFavoriteMovie(movie)
        view.reloadFavoriteMoviesList()
    }
}