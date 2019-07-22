package com.vitor.moviesapp.ui.activity.movieslist

import org.koin.dsl.module

object MoviesListModule {

    val moviesListModule = module {
        factory { MoviesListActivity() }
        factory<MoviesListContract.Presenter> { MoviesListPresenter() }
    }

}