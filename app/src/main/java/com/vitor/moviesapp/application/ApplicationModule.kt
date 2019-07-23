package com.vitor.moviesapp.application

import android.arch.persistence.room.Room
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.util.constant.DataBaseConstants
import org.koin.dsl.module

class ApplicationModule(private val application: MoviesApplication) {

    val appModule = module {
        single { application }
        single { database }
    }

    private val database = Room
        .databaseBuilder(application, AppDataBase::class.java, DataBaseConstants.DATABASE_NAME)
        .allowMainThreadQueries()
        .build()

}