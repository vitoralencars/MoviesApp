package com.vitor.moviesapp.application

import org.koin.dsl.module

class ApplicationModule(private val application: MoviesApplication) {

    val appModule = module {
        single { application }
    }

}