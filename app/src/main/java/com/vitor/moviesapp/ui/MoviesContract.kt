package com.vitor.moviesapp.ui

import com.vitor.moviesapp.base.BaseContract
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.NetworkService

class MoviesContract: BaseContract() {

    interface View: BaseView{
        fun listMovies(movies: List<Movie>)
    }

    interface Presenter: BasePresenter<View>{
        fun attachNetworkService(networkService: NetworkService)
        fun getMovies()
    }

}