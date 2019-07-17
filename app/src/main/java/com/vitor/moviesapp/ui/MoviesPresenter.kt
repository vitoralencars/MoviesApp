package com.vitor.moviesapp.ui

import android.annotation.SuppressLint
import com.vitor.moviesapp.network.NetworkService
import com.vitor.moviesapp.util.LanguageConstants
import com.vitor.moviesapp.util.NetworkConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MoviesPresenter: MoviesContract.Presenter {

    lateinit var view: MoviesContract.View
    lateinit var networkService: NetworkService

    override fun attachView(view: MoviesContract.View) {
        this.view = view
    }

    override fun attachNetworkService(networkService: NetworkService) {
        this.networkService = networkService
    }

    @SuppressLint("CheckResult")
    override fun getMovies() {
        networkService.getMovies(NetworkConstants.API_KEY, 1, LanguageConstants.PT_BR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.listMovies(it.results)
            },{
                print(it)
            })
    }
}