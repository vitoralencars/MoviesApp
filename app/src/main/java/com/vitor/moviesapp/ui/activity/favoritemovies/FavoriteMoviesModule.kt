package com.vitor.moviesapp.ui.activity.favoritemovies

import org.koin.dsl.module

object FavoriteMoviesModule {

    val favoriteMoviesModule = module{
        factory { FavoriteMoviesActivity() }
        factory<FavoriteMoviesContract.Presenter> { FavoriteMoviesPresenter() }
    }

}