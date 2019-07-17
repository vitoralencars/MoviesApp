package com.vitor.moviesapp.ui

import com.vitor.moviesapp.ui.adapter.MoviesAdapter
import org.koin.dsl.module

object MoviesModule {

    val moviesModule = module {
        factory { MoviesActivity() }
        factory<MoviesContract.Presenter> { MoviesPresenter() }
    }

}