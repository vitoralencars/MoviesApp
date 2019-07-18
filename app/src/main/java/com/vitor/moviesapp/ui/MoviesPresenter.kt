package com.vitor.moviesapp.ui

import android.annotation.SuppressLint
import com.vitor.moviesapp.model.Genre
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.DiscoverService
import com.vitor.moviesapp.network.GenreService
import com.vitor.moviesapp.util.GenreUtils
import com.vitor.moviesapp.util.LanguageConstants
import com.vitor.moviesapp.util.NetworkConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MoviesPresenter: MoviesContract.Presenter {

    lateinit var view: MoviesContract.View
    lateinit var discoverService: DiscoverService
    lateinit var genreService: GenreService

    override fun attachView(view: MoviesContract.View) {
        this.view = view
    }

    override fun attachServices(discoverService: DiscoverService, genreService: GenreService) {
        this.discoverService = discoverService
        this.genreService = genreService
    }

    override fun getMovies() {
        view.showProgressBar()
        discoverService.getMovies(NetworkConstants.API_KEY, 1, LanguageConstants.PT_BR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.listMovies(it.results)
                view.hideProgressBar()
            },{
                view.hideProgressBar()
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
            if(movie.genreIds.contains(it.id)){
                genresList.add(it)
            }
        }

        return genresList
    }
}