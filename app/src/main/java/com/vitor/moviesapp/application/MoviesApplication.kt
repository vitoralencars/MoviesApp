package com.vitor.moviesapp.application

import android.app.Application
import com.vitor.moviesapp.network.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoviesApplication)
            modules(listOf(
                ApplicationModule(this@MoviesApplication).appModule,
                NetworkModule.networkModule)
            )
        }
    }
}